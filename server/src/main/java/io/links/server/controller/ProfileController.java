package io.links.server.controller;

import io.links.server.dto.ProfileDto;
import io.links.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{username}/avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String username) {
        return ResponseEntity.ok(userService.getAvatar(username));
    }
}
