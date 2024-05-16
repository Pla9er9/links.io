package io.links.server.controller;

import io.links.server.dto.AuthResponse;
import io.links.server.dto.UserLoginRequest;
import io.links.server.dto.UserRegistrationRequest;
import io.links.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest registrationDTO
    ) {
        var authResponse = userService.registerUser(registrationDTO);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody UserLoginRequest loginDto
    ) {
        var authResponse = userService.loginUser(loginDto);
        return ResponseEntity.ok(authResponse);
    }
}
