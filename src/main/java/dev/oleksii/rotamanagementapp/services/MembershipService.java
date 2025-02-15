package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;

import java.util.Set;
import java.util.UUID;

public interface MembershipService {
    void createMembership(User user, Team team, TeamRole role);
    void deleteMembership(User user, UUID teamId);
    Member findMembership(User user, UUID teamId);
    Set<Member> getAllMemberships(User user);
}
