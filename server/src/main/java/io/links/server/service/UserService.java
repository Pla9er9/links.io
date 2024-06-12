package io.links.server.service;

import io.links.server.dto.*;
import io.links.server.model.User;
import io.links.server.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
