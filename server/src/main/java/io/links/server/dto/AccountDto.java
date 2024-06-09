package io.links.server.dto;

import io.links.server.model.Link;

import java.time.LocalDateTime;
import java.util.List;

public record AccountDto(
        String id,
        String username,
        String email,
        String description,
        List<Link> links,
        LocalDateTime joinedDateTime,
        boolean isEmailVerified,
        boolean hasAccountAvatar
        ) {}
