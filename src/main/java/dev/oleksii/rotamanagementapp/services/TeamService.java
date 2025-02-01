package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;

public interface TeamService {

    void createTeam(String email, CreateTeamRequest request);
    void deleteTeam(String email);
}
