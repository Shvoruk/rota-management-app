package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.TeamService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Team getTeamById(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team with ID " + teamId + " not found."));
    }

    @Override
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    @Override
    public void deleteTeamById(UUID teamId) {
        teamRepository.deleteById(teamId);
    }
}
