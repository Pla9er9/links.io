package io.links.server.controller;

import io.links.server.dto.AuthResponse;
import io.links.server.dto.TokenDto;
import io.links.server.dto.UserLoginRequest;
import io.links.server.dto.UserRegistrationRequest;
import io.links.server.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest registrationDTO
    ) {
        var authResponse = authService.registerUser(registrationDTO);
        authService.sendRegistrationVerificationMail(authResponse.getId());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserLoginRequest loginDto
    ) {
        var authResponse = authService.loginUser(loginDto);
        authService.sendLoginVerificationMail(authResponse.getId());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/confirm-auth")
    public ResponseEntity<TokenDto> confirmCode(
        @RequestParam String verificationCode,
        @RequestParam String userId
    ) {
        var token = authService.confirmCode(
                verificationCode,
                userId
        );

        return ResponseEntity.ok(token);
    }
}
