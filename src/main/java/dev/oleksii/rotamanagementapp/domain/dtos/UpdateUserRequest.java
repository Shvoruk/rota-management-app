package dev.oleksii.rotamanagementapp.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 50, message = "Name must be at least 2 characters long.")
    private String fullName;

    @Email(message = "Email format is invalid.")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character, and no whitespace."
    )
    private String password;

}


