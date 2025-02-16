package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;

import java.util.UUID;

public interface MemberShiftService {
    MemberShift assignShift(UUID shiftId, AssignShiftRequest request);
    void unassignShift(UUID shiftId, UUID memberShiftId);
}
