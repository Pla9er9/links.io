package io.links.server.controller;

import io.links.server.dto.ChangeEmailRequest;
import io.links.server.dto.ChangePasswordRequest;
import io.links.server.dto.EditProfileRequest;
import io.links.server.dto.ProfileDto;
import io.links.server.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PutMapping("/profile")
    @CachePut(value= "Profile", key="#authentication.name")
    public ProfileDto editProfile(
            @Valid @RequestBody EditProfileRequest editProfileRequest,
            Authentication authentication
    ) {
        return accountService.editProfile(
                editProfileRequest,
                authentication.getName()
        );
    }

    @DeleteMapping
    @CacheEvict(value= "Profile", key="#authentication.name", allEntries = true)
    public void deleteProfile(Authentication authentication) {
        accountService.deleteProfile(authentication.getName());
    }

    @PostMapping("/avatar")
    public void uploadAvatar(MultipartFile file, Authentication authentication) {
        accountService.uploadAvatar(file, authentication.getName());
    }

    @DeleteMapping("/avatar")
    public void deleteAvatar(Authentication authentication) {
        accountService.deleteAvatar(authentication.getName());
    }

    @PutMapping("/email")
    public void changeEmail(
            @Valid @RequestBody ChangeEmailRequest changeEmailRequest,
            Authentication authentication
    ) {
        accountService.changeEmail(
                changeEmailRequest.getEmail(),
                authentication.getName()
        );
    }

    @PutMapping("/password")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            Authentication authentication
    ) {
        accountService.changePassword(
                changePasswordRequest.getCurrentPassword(),
                changePasswordRequest.getPassword(),
                authentication.getName()
        );
    }
}
