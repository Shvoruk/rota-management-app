package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.TeamMember;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamMemberRepository;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.UserNotFoundException;
import dev.oleksii.rotamanagementapp.services.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

    private final UserRepository userRepository;

    private final TeamMemberRepository teamMemberRepository;
    @Override
    public TeamMember createMembership(String email, Team team, TeamRole role) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Couldn't create membership for user with email " + email));
        var membership = TeamMember.builder()
                            .user(user)
                            .team(team)
                            .role(role)
                            .build();

        user.addMembership(membership);

        return membership;
    }
}
