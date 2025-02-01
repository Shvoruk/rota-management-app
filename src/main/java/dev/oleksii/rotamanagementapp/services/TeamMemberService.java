package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;

public interface TeamMemberService {
    TeamMember createMembership(User user, Team team, TeamRole role);

    TeamMember findMembership(User user, Team team);

    void deleteMembership(User user, Team team);

    void deleteAllMemberships(Team team);
}
