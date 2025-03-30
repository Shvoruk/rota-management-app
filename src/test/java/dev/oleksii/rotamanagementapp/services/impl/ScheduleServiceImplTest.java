package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceImplTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    void getScheduleByTeamId() {
        Team team = new Team();
        team.setId(UUID.randomUUID());
        Schedule schedule = new Schedule();
        schedule.setTeam(team);

        Mockito.when(scheduleRepository.findByTeamId(team.getId()))
                .thenReturn(Optional.of(schedule));

        var result = scheduleService.getScheduleByTeamId(team.getId());

        assertNotNull(result);
        assertEquals(schedule, result);
    }

    @Test
    void getScheduleByTeamIdThrows() {
        var teamId = UUID.randomUUID();

        Mockito.when(scheduleRepository.findByTeamId(teamId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> scheduleService.getScheduleByTeamId(teamId));
        verify(scheduleRepository).findByTeamId(teamId);
    }
}