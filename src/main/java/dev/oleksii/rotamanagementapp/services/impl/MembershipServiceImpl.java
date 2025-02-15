package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import dev.oleksii.rotamanagementapp.exceptions.MembershipAlreadyExistsException;
import dev.oleksii.rotamanagementapp.exceptions.MembershipNotFoundException;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    public void createMembership(User user, Team team, TeamRole role) {
        if (membershipRepository.existsByUserAndTeam(user, team)) {
            throw new MembershipAlreadyExistsException("User is already a member of this team");
        }
       var member = Member.builder()
               .fullName(user.getFullName())
               .user(user)
               .team(team)
               .role(role)
               .build();
       membershipRepository.save(member);
    }

    @Override
    public Member findMembership(User user, UUID teamId) {
        return membershipRepository.findByUserAndTeamId(user, teamId).orElseThrow(() -> new MembershipNotFoundException("User is not a member of this team"));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Member> getAllMemberships(User user) {
        return membershipRepository.findAllMembershipsByUser(user);
    }

    @Override
    public void deleteMembership(User user, UUID teamId) {
        if (!membershipRepository.existsByUserAndTeamId(user, teamId)) {
            throw new MembershipNotFoundException("User is not a member of this team");
        }
        membershipRepository.deleteByUserAndTeamId(user, teamId);
    }
}
