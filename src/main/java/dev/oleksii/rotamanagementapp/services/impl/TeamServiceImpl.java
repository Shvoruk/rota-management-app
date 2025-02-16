package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public Team findTeamById(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team with ID " + teamId + " not found"));
    }

    @Transactional
    @Override
    public Team createTeam(User user, CreateTeamRequest request) {
        var team = Team.builder()
                .name(request.getName())
                .build();
        var schedule = Schedule.builder()
                .team(team)
                .build();
        var member = Member.builder()
                .fullName(user.getFullName())
                .user(user)
                .team(team)
                .role(TeamRole.MANAGER)
                .build();

        team.setSchedule(schedule);
        team.addMember(member);
        return teamRepository.save(team);
    }

    @Transactional
    @Override
    public void deleteTeam(UUID teamId) {
        teamRepository.deleteById(teamId);
    }

    @Transactional
    @Override
    public Team joinTeam(User user, UUID teamId) {
        var team = findTeamById(teamId);
        var member = Member.builder()
                .fullName(user.getFullName())
                .user(user)
                .team(team)
                .role(TeamRole.EMPLOYEE)
                .build();
        team.addMember(member);
        return teamRepository.save(team);
    }

    @Transactional
    @Override
    public void leaveTeam(Member member, UUID teamId) {
        var team = findTeamById(teamId);
        team.removeMember(member);
        teamRepository.save(team);
    }

    @Override
    public Team getTeam(UUID teamId) {
        return findTeamById(teamId);
    }

    @Override
    public Set<Team> getAllTeams(User user) {
        return user.getMemberships().stream()
                .map(Member::getTeam)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Member> getAllTeamMembers(UUID teamId) {
        return findTeamById(teamId).getMembers();
    }
}
