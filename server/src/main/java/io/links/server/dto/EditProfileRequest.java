package io.links.server.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.links.server.model.Link;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EditProfileRequest {
    @NotNull
    @Size(min = 3, max = 22, message = "Username must be between 6 and 50 characters")
    private String username;
    @NotNull
    @Size(max = 150)
    private String description;
    @NotNull
    @Size(max = 6)
    private List<Link> links;
}
