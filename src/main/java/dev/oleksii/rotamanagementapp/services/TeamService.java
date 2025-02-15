package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.util.Set;
import java.util.UUID;

public interface TeamService {
    TeamDto createTeam(User user, CreateTeamRequest request);
    void deleteTeam(UUID teamId);
    Set<TeamDto> getAllTeams(User user);
    Set<MemberDto> getAllTeamMembers(UUID teamId);
    TeamDto getTeam(UUID teamId);
    TeamDto joinTeam(User user, UUID teamId);
    void leaveTeam(User user, UUID teamId);

}

