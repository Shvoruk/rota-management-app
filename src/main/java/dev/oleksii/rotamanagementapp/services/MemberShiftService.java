package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;

import java.util.UUID;

public interface MemberShiftService {
    MemberShiftDto assignShift(UUID shiftId, AssignShiftRequest request);
    void unassignShift(UUID shiftId, UUID memberShiftId);
}
