package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    public Member getMembershipById(UUID memberId) {
        return membershipRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Membership with ID " + memberId + " not found."));
    }

    @Override
    public Member getMembershipByUserIdAndTeamId(UUID userId, UUID teamId) {
        return membershipRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new NotFoundException("User is not a member of team with ID " + teamId));
    }

    public Set<Member> getAllMembershipsByTeamId(UUID teamId) {
        return membershipRepository.findAllByTeamId(teamId);
    }

    @Override
    public void deleteMembershipByUserIdAndTeamId(UUID userId, UUID teamId) {
        membershipRepository.deleteByUserIdAndTeamId(userId, teamId);
    }
}
