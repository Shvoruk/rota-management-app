package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.services.TeamMemberService;
import dev.oleksii.rotamanagementapp.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberService teamMemberService;

    @Override
    public void createTeam(String email, CreateTeamRequest request) {

        var team = Team.builder()
                .name(request.getName())
                .build();

        team.addMember(teamMemberService.createMembership(email, team, TeamRole.MANAGER));

        teamRepository.save(team);
    }

    @Override
    public void deleteTeam(String email) {

    }
}
