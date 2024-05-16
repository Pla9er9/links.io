package io.links.server.service;

import io.links.server.dto.AuthResponse;
import io.links.server.dto.UniqueUserProperties;
import io.links.server.dto.UserLoginRequest;
import io.links.server.dto.UserRegistrationRequest;
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
}
