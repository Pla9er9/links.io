package io.links.server.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EditProfileRequest {
    @NotNull
    private String description;
    @Size(max = 6)
    private List<String> links;
}
