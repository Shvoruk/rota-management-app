package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Member;

import java.util.Set;
import java.util.UUID;

public interface MembershipService {
    Member getMembershipById(UUID membershipId);
    Member getMembershipByUserIdAndTeamId(UUID userId, UUID teamId);
    Set<Member> getAllMembershipsByTeamId(UUID teamId);
    void deleteMembershipByUserIdAndTeamId(UUID userId, UUID teamId);
}
