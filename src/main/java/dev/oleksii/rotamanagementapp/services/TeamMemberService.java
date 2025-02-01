package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;

public interface TeamMemberService {
    TeamMember createMembership(String email, Team team, TeamRole role);
}
