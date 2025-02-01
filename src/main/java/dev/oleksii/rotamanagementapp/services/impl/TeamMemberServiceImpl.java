package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamMemberRepository;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.services.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Override
    public TeamMember createMembership(User user, Team team, TeamRole role) {

        var membership = TeamMember.builder()
                            .user(user)
                            .team(team)
                            .role(role)
                            .build();

        user.addMembership(membership);

        return membership;
    }

    @Override
    public TeamMember findMembership(User user, Team team) {
        return teamMemberRepository.findByUserAndTeam(user, team).orElseThrow();
    }

    @Override
    public void deleteMembership(User user, Team team) {
        teamMemberRepository.deleteByUserAndTeam(user, team);
        user.removeMembership(findMembership(user, team));
    }

    @Override
    public void deleteAllMemberships(Team team) {
        team.getMembers().forEach(member -> {
            deleteMembership(member.getUser(),team);
        });
    }
}
