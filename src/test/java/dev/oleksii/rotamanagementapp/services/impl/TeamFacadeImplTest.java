package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.mappers.MemberMapper;
import dev.oleksii.rotamanagementapp.mappers.ScheduleMapper;
import dev.oleksii.rotamanagementapp.mappers.TeamMapper;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import dev.oleksii.rotamanagementapp.services.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamFacadeImplTest {

    @Mock
    private TeamMapper teamMapper;
    @Mock
    private TeamService teamService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private ScheduleMapper scheduleMapper;
    @Mock
    private MemberMapper memberMapper;
    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private TeamFacadeImpl teamFacade;

    private static final UUID TEAM_ID = UUID.randomUUID();
    @Test
    void getTeam() {
        Team team = new Team();
        TeamDto teamDto = new TeamDto();

        when(teamService.getTeamById(TEAM_ID))
                .thenReturn(team);
        when(teamMapper.toTeamDTO(team))
                .thenReturn(teamDto);

        var result = teamFacade.getTeam(TEAM_ID);

        assertEquals(teamDto, result);
        verify(teamService).getTeamById(TEAM_ID);
        verify(teamMapper).toTeamDTO(team);
    }

    @Test
    void getTeamSchedule() {
        Schedule schedule = new Schedule();
        ScheduleDto scheduleDto = new ScheduleDto();

        when(scheduleService.getScheduleByTeamId(TEAM_ID))
                .thenReturn(schedule);
        when(scheduleMapper.toScheduleDTO(schedule))
                .thenReturn(scheduleDto);

        var result = teamFacade.getTeamSchedule(TEAM_ID);

        assertEquals(scheduleDto, result);
        verify(scheduleService).getScheduleByTeamId(TEAM_ID);
        verify(scheduleMapper).toScheduleDTO(schedule);
    }

    @Test
    void getAllTeamMembers() {
        Member member = new Member();
        Set<Member> members = Set.of(member);
        Set<MemberDto> memberDtos = new HashSet<>();

        when(membershipService.getAllMembershipsByTeamId(TEAM_ID))
                .thenReturn(members);
        when(memberMapper.toMembersDTO(members))
                .thenReturn(memberDtos);

        var result = teamFacade.getAllTeamMembers(TEAM_ID);

        assertEquals(memberDtos, result);
        verify(membershipService).getAllMembershipsByTeamId(TEAM_ID);
        verify(memberMapper).toMembersDTO(members);
    }

    @Test
    void getAllTeams() {
        User user = new User();
        Set<TeamDto> expectedDtos = new HashSet<>();

        when(teamMapper.toTeamsDTO(anySet()))
                .thenReturn(expectedDtos);

        var result = teamFacade.getAllTeams(user);

        assertEquals(expectedDtos, result);
        verify(teamMapper).toTeamsDTO(anySet());
    }

    @Test
    void createTeam() {
        TeamDto teamDto = new TeamDto();

        doNothing().when(teamService).saveTeam(any(Team.class));
        when(teamMapper.toTeamDTO(any(Team.class)))
                .thenReturn(teamDto);

        TeamDto result = teamFacade.createTeam(new User(), "");

        assertEquals(teamDto, result);
        verify(teamService).saveTeam(any(Team.class));
        verify(teamMapper).toTeamDTO(any(Team.class));
    }

    @Test
    void deleteTeam() {
        teamFacade.deleteTeam(TEAM_ID);

        verify(teamService).deleteTeamById(TEAM_ID);
    }

    @Test
    void joinTeam() {
        Team team = new Team();
        TeamDto teamDto = new TeamDto();

        when(teamService.getTeamById(TEAM_ID))
                .thenReturn(team);
        doNothing().when(teamService)
                .saveTeam(team);
        when(teamMapper.toTeamDTO(team))
                .thenReturn(teamDto);

        TeamDto result = teamFacade.joinTeam(new User(), TEAM_ID);

        assertEquals(teamDto, result);
        verify(teamService).getTeamById(TEAM_ID);
        verify(teamService).saveTeam(team);
        verify(teamMapper).toTeamDTO(team);
    }

    @Test
    void leaveTeam() {
        User user = new User();

        teamFacade.leaveTeam(user, TEAM_ID);

        verify(membershipService).deleteMembershipByUserIdAndTeamId(user.getId(), TEAM_ID);
    }
}