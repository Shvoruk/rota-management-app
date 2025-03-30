package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Team;
import dev.oleksii.rotamanagementapp.domain.repos.TeamRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void getTeamById() {
        UUID teamId = UUID.randomUUID();
        Team team = new Team();
        team.setId(teamId);

        when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(team));

        var result = teamService.getTeamById(teamId);

        verify(teamRepository).findById(teamId);
        assertNotNull(result);
        assertEquals(team, result);
        assertEquals(result.getId(), teamId);
    }

    @Test
    void getShiftByTeamIdAndShiftIdThrows(){
        UUID teamId = UUID.randomUUID();

        when(teamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> teamService.getTeamById(teamId));
        verify(teamRepository).findById(teamId);
    }

    @Test
    void saveTeam() {
        Team team = new Team();
        teamRepository.save(team);
        verify(teamRepository).save(team);
    }

    @Test
    void deleteTeamById() {
        UUID teamId = UUID.randomUUID();
        teamService.deleteTeamById(teamId);
        verify(teamRepository).deleteById(teamId);
    }
}