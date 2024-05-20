package io.links.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@RedisHash("Profile")
public class ProfileDto implements Serializable {
    private String username;
    private String description;
    private List<String> links;
    private LocalDate joinedDate;
}
