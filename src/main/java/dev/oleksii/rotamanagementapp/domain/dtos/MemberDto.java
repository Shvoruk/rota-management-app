package dev.oleksii.rotamanagementapp.domain.dtos;

import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    private UUID memberId;

    private String fullName;

    private TeamRole role;

}
