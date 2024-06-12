package io.links.server.service;

import io.links.server.dto.AuthResponse;
import io.links.server.dto.UserLoginRequest;
import io.links.server.dto.UserRegistrationRequest;
import io.links.server.exception.ValidationException;
import io.links.server.model.User;
import io.links.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerUser(UserRegistrationRequest userDTO) {
        checkIfUserUniquePropertiesAreNotUsed(
                userDTO.getUsername(),
                userDTO.getEmail()
        );

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

    public void checkIfUserUniquePropertiesAreNotUsed(String username, String email) {
        var userOptional = userRepository.findByUsername(username);
        throwIfUserPresent(userOptional.isPresent(), "Username");

        userOptional = userRepository.findByEmail(email);
        throwIfUserPresent(userOptional.isPresent(), "Email");
    }

    public void throwIfUserPresent(boolean isPresent, String propertyName) {
        if (!isPresent) {
            return;
        }
        String message = propertyName + " already taken";
        throw new ValidationException(message);
    }
}
