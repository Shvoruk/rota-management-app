package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.util.Set;
import java.util.UUID;

public interface TeamService {
    Team createTeam(User user, CreateTeamRequest request);
    Team getTeam(UUID teamId);
    void deleteTeam(UUID teamId);
    Set<Team> getAllTeams(User user);
    Set<Member> getAllTeamMembers(UUID teamId);
    Team joinTeam(User user, UUID teamId);
    void leaveTeam(Member member, UUID teamId);
}

