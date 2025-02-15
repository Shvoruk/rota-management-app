package dev.oleksii.rotamanagementapp.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTeamRequest {

    @NotBlank(message = "Team name is required.")
    @Size(min = 2, max = 50, message = "Team name must be at least 2 characters long.")
    private String name;

}
