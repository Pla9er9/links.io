package io.links.server.service;

import io.links.server.dto.AuthResponse;
import io.links.server.dto.TokenDto;
import io.links.server.dto.UserLoginRequest;
import io.links.server.dto.UserRegistrationRequest;
import io.links.server.exception.ValidationException;
import io.links.server.model.User;
import io.links.server.model.VerificationCode;
import io.links.server.repository.UserRepository;
import io.links.server.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MailService mailService;
    private final Random random = new Random();
    @Value("${webapp.url}")
    private String webappUrl;

    public AuthResponse registerUser(UserRegistrationRequest userDTO) {
        areUniquePropertiesNotTaken(
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

        return new AuthResponse(
                user.getId(),
                user.getUsername()
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
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST
                        ));

        return new AuthResponse(
                user.getId(),
                user.getUsername()
        );
    }

    public void areUniquePropertiesNotTaken(String username, String email) {
        var userOptional = userRepository.findByUsername(username);
        throwIfUserPresent(userOptional.isPresent(), "Username");

        userOptional = userRepository.findByEmail(email);
        throwIfUserPresent(userOptional.isPresent(), "Email");
    }

    public void throwIfUserPresent(boolean isPresent, String propertyName) {
        if (isPresent) {
            String message = propertyName + " already taken";
            throw new ValidationException(message);
        }
    }

    public void sendLoginVerificationMail(String userId) {
        sendVerificationMail(userId, "confirmLogin");
    }

    public void sendRegistrationVerificationMail(String userId) {
        sendVerificationMail(userId, "confirmRegistration");
    }

    private void sendVerificationMail(String userId, String template) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        var verificationCode = createAndSaveVerificationCode(user);

        var confirmCodeUrl = webappUrl + "/api/auth/confirm-auth?code="
                + verificationCode.getCode() + "&userId=" + userId;

        var variables = new HashMap<String, Object>();
        variables.put("confirmUrl", confirmCodeUrl);

        mailService.sendTemplateMail(
                template,
                user.getEmail(),
                "Links.io confirm to continue",
                variables
        );
    }

    public VerificationCode createAndSaveVerificationCode(User user) {
        String randomCode = generateVerificationCode();
        VerificationCode verificationCode = VerificationCode
                .builder()
                .code(randomCode)
                .created_dateTime(LocalDateTime.now())
                .user(user)
                .build();

        return verificationCodeRepository.save(verificationCode);
    }

    public String generateVerificationCode() {
        final int codeLength = 10;
        final String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        final StringBuilder sb = new StringBuilder();

        for (int i = 0; i < codeLength; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public TokenDto confirmCode(String code, String userId) {
        VerificationCode verificationCode = verificationCodeRepository
                .findByCodeAndUser_id(code, userId).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        hasCodeExpired(verificationCode);
        var user = verificationCode.getUser();

        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            userRepository.save(user);
        }

        var jwtToken = jwtService.generateToken(user);

        verificationCodeRepository.delete(verificationCode);

        return new TokenDto(jwtToken);
    }

    public void hasCodeExpired(VerificationCode verificationCode) {
        final int minutesBeforeCodeExpire = 15;

        var codeCreatedDateTime = verificationCode.getCreated_dateTime();
        var maxTimeBeforeExpired = LocalDateTime.now()
                .minusMinutes(minutesBeforeCodeExpire);

        if (codeCreatedDateTime.isBefore(maxTimeBeforeExpired)) {
            verificationCodeRepository.delete(verificationCode);
            var user = verificationCode.getUser();
            deleteAccountIfNotVerified(user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public void deleteAccountIfNotVerified(User user) {
        if (!user.isEmailVerified()) {
            userRepository.delete(user);
        }
    }

}
