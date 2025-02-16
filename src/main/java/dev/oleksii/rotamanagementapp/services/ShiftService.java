package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.Shift;

import java.util.UUID;

public interface ShiftService {
    ShiftDto createShift(CreateShiftRequest request);
    ShiftDto getShiftDto(UUID shiftId);
    Shift getShift(UUID shiftId);
    void deleteShift(UUID shiftId);
}

