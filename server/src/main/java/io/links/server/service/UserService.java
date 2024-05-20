package io.links.server.service;

import io.links.server.dto.*;
import io.links.server.exception.ValidationException;
import io.links.server.model.User;
import io.links.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerUser(UserRegistrationRequest userDTO) {
        var uniqueProperties = new UniqueUserProperties(userDTO.getUsername(), userDTO.getEmail());
        checkIfUserUniquePropertiesAreNotUsed(uniqueProperties);

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

    User user = User.builder()
            .username(userDTO.getUsername())
            .email(userDTO.getEmail())
            .password(encodedPassword)
            .joinedDateTime(LocalDateTime.now())
            .isEmailVerified(false)
            .description("")
            .links(List.of())
            .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                jwtToken
        );
    }

    public AuthResponse loginUser(UserLoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

    User user =
        userRepository
            .findByUsername(loginRequest.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                jwtToken
        );
    }

    public void checkIfUserUniquePropertiesAreNotUsed(UniqueUserProperties properties) {
        var userOptional = userRepository.findByUsername(properties.username());
        throwIfUserPresent(userOptional.isPresent(), "Username");

        userOptional = userRepository.findByEmail(properties.email());
        throwIfUserPresent(userOptional.isPresent(), "Email");

    }

    public void throwIfUserPresent(boolean isPresent, String propertyName) {
        if (isPresent) {
            String message = propertyName + " already taken";
            throw new ValidationException(message);
        }
    }

    public ProfileDto getProfile(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var user = userOptional.get();

        return new ProfileDto(
            user.getUsername(),
            user.getDescription(),
            user.getLinks(),
            user.getJoinedDateTime().toLocalDate()
        );
    }

    public byte[] getAvatar(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var avatar = user.getAvatar();

        if (avatar == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return avatar.getData();
    }
}
