package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.exceptions.TeamNotFoundException;
import dev.oleksii.rotamanagementapp.mappers.TeamMapper;
import dev.oleksii.rotamanagementapp.mappers.MemberMapper;
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
        teamRepository.save(team);
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
        if(team.getMembers().stream().anyMatch(member -> member.getUser().equals(user))) {
            throw new IllegalArgumentException("User is already a member of this team");
        }
        var member = Member.builder()
                .fullName(user.getFullName())
                .user(user)
                .team(team)
                .role(TeamRole.EMPLOYEE)
                .build();

        team.addMember(member);
        teamRepository.save(team);
        return teamMapper.toTeamDTO(team);
    }

    @Transactional
    @Override
    public void leaveTeam(User user, UUID teamId) {
        var team = findTeamById(teamId);
        var member = team.getMembers().stream()
                .filter(m -> m.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this team"));

        team.removeMember(member);
        teamRepository.save(team);
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
