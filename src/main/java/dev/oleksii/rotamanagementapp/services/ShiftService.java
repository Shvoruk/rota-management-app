package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Shift;

import java.util.UUID;

public interface ShiftService {
    Shift getShiftByTeamIdAndShiftId(UUID teamId, UUID shiftId);
    void saveShift(Shift shift);
    void deleteShiftById(UUID shiftId);
}
