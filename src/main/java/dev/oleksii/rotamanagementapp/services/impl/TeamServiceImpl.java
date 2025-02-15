package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.exceptions.TeamNotFoundException;
import dev.oleksii.rotamanagementapp.mappers.TeamMapper;
import dev.oleksii.rotamanagementapp.mappers.MemberMapper;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import dev.oleksii.rotamanagementapp.services.MembershipService;
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
    private final MembershipService membershipService;
    private final ScheduleService scheduleService;
    private final TeamMapper teamMapper;
    private final MemberMapper memberMapper;

    public Team findTeamById(UUID teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException("Team with ID " + teamId + " not found"));
    }

    @Transactional
    @Override
    public TeamDto createTeam(User user, CreateTeamRequest request) {
        var team = Team.builder()
                .name(request.getName())
                .build();
        teamRepository.save(team);
        membershipService.createMembership(user, team, TeamRole.MANAGER);
        scheduleService.createSchedule(team);
        return teamMapper.toTeamDTO(team);
    }

    @Transactional
    @Override
    public void deleteTeam(UUID teamId) {
        teamRepository.deleteById(teamId);
    }

    @Transactional
    @Override
    public TeamDto joinTeam(User user, UUID teamId) {
        var team = findTeamById(teamId);
        membershipService.createMembership(user, team, TeamRole.EMPLOYEE);
        return teamMapper.toTeamDTO(team);
    }

    @Transactional
    @Override
    public void leaveTeam(User user, UUID teamId) {
        membershipService.deleteMembership(user, teamId);
    }

    @Transactional(readOnly = true)
    @Override
    public TeamDto getTeam(UUID teamId) {
        return teamMapper.toTeamDTO(findTeamById(teamId));
    }

    @Transactional(readOnly = true)
    @Override
    public Set<TeamDto> getAllTeams(User user) {
        Set<Member> memberships = membershipService.getAllMemberships(user);
        Set<Team> teams = memberships.stream()
                .map(Member::getTeam)
                .collect(Collectors.toSet());

        return teams.stream()
                .map(teamMapper::toTeamDTO)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public Set<MemberDto> getAllTeamMembers(UUID teamId) {
        var team = findTeamById(teamId);
        return team.getMembers().stream()
                .map(memberMapper::toMemberDTO)
                .collect(Collectors.toSet());
    }
}
