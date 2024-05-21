package io.links.server.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    @NotNull
    @Size(min = 3, max = 22, message = "Username must be between 6 and 50 characters")
    private String username;
    @NotNull
    @Email(message = "Email must be valid")
    private String email;
    @NotNull
    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;
}
