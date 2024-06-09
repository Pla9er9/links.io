package io.links.server.service;

import io.links.server.dto.AccountDto;
import io.links.server.dto.EditProfileRequest;
import io.links.server.dto.ProfileDto;
import io.links.server.exception.ValidationException;
import io.links.server.model.Image;
import io.links.server.model.User;
import io.links.server.repository.ImageRepository;
import io.links.server.repository.UserRepository;
import io.links.server.validator.UploadedFileValidator;
import io.links.server.worker.ImageTaskWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public ProfileDto editProfile(EditProfileRequest editProfileRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

        String passedUsername = editProfileRequest.getUsername();

        if (!username.equals(passedUsername)) {
            var optionalUser = userRepository.findByUsername(passedUsername);
            if (optionalUser.isPresent()) {
                throw new ValidationException("Username already taken");
            }

            user.setUsername(passedUsername);
        }

        user.setDescription(editProfileRequest.getDescription());
        user.setLinks(editProfileRequest.getLinks());

        userRepository.save(user);

        return new ProfileDto(
                user.getUsername(),
                user.getDescription(),
                user.getLinks(),
                user.getJoinedDateTime().toLocalDate()
        );
    }

    public void deleteProfile(String username) {
        userRepository.deleteByUsername(username);
    }

    public void deleteAvatar(String name) {
        User user = userRepository.findByUsername(name).orElseThrow();
        user.setAvatar(null);

        userRepository.save(user);
    }

    public void uploadAvatar(MultipartFile file, String name) {
        final byte[] bytes;

        UploadedFileValidator.validate(file, "image/");

        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        User user = userRepository.findByUsername(name).orElseThrow();

        var binary = new Binary(
                BsonBinarySubType.BINARY,
                bytes
        );

        var image = Image
                .builder()
                .user(user)
                .image(binary)
                .uploadTime(LocalDateTime.now())
                .build();

        image = imageRepository.save(image);

        ImageTaskWorker.queueOfImagesToProcess.add(image.getId());
    }

    public void changeEmail(String email, String username) {
        final User user = userRepository.findByUsername(username)
                .orElseThrow();

        user.setEmail(email);
        userRepository.save(user);
    }

    public void changePassword(
            String currentPassword,
            String newPassword,
            String username
    ) {
        final User user;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            currentPassword
                    )
            );
        } catch (AuthenticationException e) {
            throw new ValidationException("Wrong current password");
        } finally {
            user = userRepository.findByUsername(username)
                    .orElseThrow();

            user.setPassword(
                    passwordEncoder.encode(newPassword)
            );
            userRepository.save(user);
        }
    }

    public AccountDto getAccount(String name) {
        var user = userRepository.findByUsername(name)
                .orElseThrow();

        return new AccountDto(
            user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDescription(),
                user.getLinks(),
                user.getJoinedDateTime(),
                user.isEmailVerified(),
                user.getAvatar() != null
        );
    }
}
