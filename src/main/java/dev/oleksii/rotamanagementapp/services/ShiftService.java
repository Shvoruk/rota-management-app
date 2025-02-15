package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;

import java.util.UUID;

public interface ShiftService {
    ShiftDto createShift(CreateShiftRequest request);
    void deleteShift(UUID shiftId);
    ShiftDto assignShift(AssignShiftRequest request);
    void unassignShift(UUID memberShiftId);
}
