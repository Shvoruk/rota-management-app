package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.services.TeamMemberService;
import dev.oleksii.rotamanagementapp.services.TeamService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberService teamMemberService;
    private final EntityManager em;

    @Transactional
    @Override
    public void createTeam(User user, String name) {

        var team = Team.builder()
                .name(name)
                .build();

        team.addMember(teamMemberService.createMembership(user, team, TeamRole.MANAGER));
        teamRepository.save(team);

    }

    @Transactional
    @Override
    public void deleteTeam(User user, UUID teamId) {

        var team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalArgumentException("Team with ID " + teamId + " not found"));
        var member = teamMemberService.findMembership(user, team);

        if(member.getRole().equals(TeamRole.MANAGER)) {
            teamMemberService.deleteAllMemberships(team);
            teamRepository.delete(team);
        }
    }
}
