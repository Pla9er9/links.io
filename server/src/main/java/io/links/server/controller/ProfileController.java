package io.links.server.controller;

import io.links.server.dto.ProfileDto;
import io.links.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile/")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @GetMapping("/{username}")
    @Cacheable(value= "Profile", key="#username")
    public ProfileDto getProfile(@PathVariable String username) {
        return userService.getProfile(username);
    }

    @GetMapping(value = "/{username}/avatar", produces = "image/*")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String username) {
        return ResponseEntity.ok(userService.getAvatar(username));
    }
}
