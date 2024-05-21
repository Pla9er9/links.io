package io.links.server.service;

import io.links.server.dto.EditProfileRequest;
import io.links.server.dto.ProfileDto;
import io.links.server.exception.ValidationException;
import io.links.server.model.User;
import io.links.server.repository.UserRepository;
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
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public ProfileDto editProfile(EditProfileRequest editProfileRequest, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();

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
        if (file == null) {
            throw new ValidationException("File not provided");
        }

        byte[] bytes;
        User user = userRepository.findByUsername(name).orElseThrow();

        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        var binary = new Binary(
                BsonBinarySubType.BINARY,
                bytes
        );

        user.setAvatar(binary);
        userRepository.save(user);
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
}
