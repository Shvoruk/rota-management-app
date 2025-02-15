package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;

import java.util.UUID;

public interface ShiftService {
    ShiftDto createShift(CreateShiftRequest request);
    ShiftDto getShift(UUID shiftId);
    void deleteShift(UUID shiftId);
}
