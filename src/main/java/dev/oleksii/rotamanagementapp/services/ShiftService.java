package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.entities.Shift;

import java.util.UUID;

public interface ShiftService {
    Shift createShift(UUID scheduleId, CreateShiftRequest request);
    Shift getShift(UUID shiftId);
    Shift getShift(UUID teamId, UUID shiftId);
    void deleteShift(UUID teamId, UUID shiftId);
}

