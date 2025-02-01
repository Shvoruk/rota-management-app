package dev.oleksii.rotamanagementapp.domain.dtos;

import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeamRequest {

    @NotBlank(message = "Team name is required.")
    private String name;

}
