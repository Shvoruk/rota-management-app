package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Team;

import java.util.UUID;

public interface TeamService {
    Team getTeamById(UUID teamId);
    void saveTeam(Team team);
    void deleteTeamById(UUID teamId);
}
