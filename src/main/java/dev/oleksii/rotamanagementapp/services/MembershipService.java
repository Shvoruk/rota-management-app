package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;

import java.util.Set;
import java.util.UUID;

public interface MembershipService {
    Set<Member> getAllMemberships(User user);
}
