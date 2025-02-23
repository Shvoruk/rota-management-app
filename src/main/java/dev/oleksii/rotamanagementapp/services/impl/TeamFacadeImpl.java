package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.mappers.MemberMapper;
import dev.oleksii.rotamanagementapp.mappers.ScheduleMapper;
import dev.oleksii.rotamanagementapp.mappers.TeamMapper;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import dev.oleksii.rotamanagementapp.services.TeamFacade;
import dev.oleksii.rotamanagementapp.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of {@link TeamFacade}.
 * <p>
 * Detailed documentation is provided in the interface.
 */
@Service
@RequiredArgsConstructor
public class TeamFacadeImpl implements TeamFacade {

    private final TeamMapper teamMapper;
    private final TeamService teamService;
    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;
    private final MemberMapper memberMapper;
    private final MembershipService membershipService;

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamDto getTeam(UUID teamId) {
        return teamMapper.toTeamDTO(teamService.getTeamById(teamId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleDto getTeamSchedule(UUID teamId) {
        return scheduleMapper.toScheduleDTO(scheduleService.getScheduleByTeamId(teamId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<MemberDto> getAllTeamMembers(UUID teamId) {
        return memberMapper.toMembersDTO(membershipService.getAllMembershipsByTeamId(teamId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<TeamDto> getAllTeams(User user) {
        Set<Member> memberships = new HashSet<>(user.getMemberships());
        return teamMapper.toTeamsDTO(
                memberships.stream()
                        .map(Member::getTeam)
                        .collect(Collectors.toSet())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamDto createTeam(User user, String name) {
        // Build a new team entity with the specified name.
        var team = Team.builder().name(name).build();

        // Create a mandatory schedule associated with the team.
        var schedule = Schedule.builder().team(team).build();

        // Create a new member for the user as a manager.
        var member = Member.builder()
                .fullName(user.getFullName())
                .user(user)
                .team(team)
                .role(TeamRole.MANAGER)
                .build();

        // Associate the schedule and member with the team.
        team.addMember(member);
        team.setSchedule(schedule);
        // Persist the team entity.
        teamService.saveTeam(team);
        return teamMapper.toTeamDTO(team);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTeam(UUID teamId) {
        teamService.deleteTeamById(teamId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TeamDto joinTeam(User user, UUID teamId) {
        // Retrieve the team entity.
        var team = teamService.getTeamById(teamId);

        // Build a new member for the user with an employee role.
        var member = Member.builder()
                .fullName(user.getFullName())
                .user(user)
                .team(team)
                .role(TeamRole.EMPLOYEE)
                .build();

        // Add the member to the team and persist the update.
        team.addMember(member);
        teamService.saveTeam(team);
        return teamMapper.toTeamDTO(team);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void leaveTeam(User user, UUID teamId) {
        // Remove the membership linking the user to the team.
        membershipService.deleteMembershipByUserIdAndTeamId(user.getId(), teamId);
    }
}
