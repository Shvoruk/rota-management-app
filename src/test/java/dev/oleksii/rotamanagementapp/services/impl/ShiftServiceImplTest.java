package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
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
class ShiftServiceImplTest {

    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftServiceImpl shiftService;

    @Test
    void getShiftByTeamIdAndShiftId() {
        UUID teamId = UUID.randomUUID();
        UUID shiftId = UUID.randomUUID();
        Shift shift = new Shift();
        shift.setId(shiftId);

        when(shiftRepository.findByTeamIdAndShiftId(teamId, shiftId))
                .thenReturn(Optional.of(shift));
        var result = shiftService.getShiftByTeamIdAndShiftId(teamId, shiftId);
        verify(shiftRepository).findByTeamIdAndShiftId(teamId, shiftId);

        assertNotNull(result);
        assertEquals(shift, result);
        assertEquals(result.getId(), shiftId);
    }

    @Test
    void getShiftByTeamIdAndShiftIdThrows(){
        UUID teamId = UUID.randomUUID();
        UUID shiftId = UUID.randomUUID();

        when(shiftRepository.findByTeamIdAndShiftId(teamId, shiftId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> shiftService.getShiftByTeamIdAndShiftId(teamId, shiftId));
        verify(shiftRepository).findByTeamIdAndShiftId(teamId, shiftId);
    }

    @Test
    void saveShift() {
        Shift shift = new Shift();
        shiftRepository.save(shift);
        verify(shiftRepository).save(shift);
    }

    @Test
    void deleteShiftById() {
        UUID shiftId = UUID.randomUUID();
        shiftService.deleteShiftById(shiftId);
        verify(shiftRepository).deleteById(shiftId);
    }
}