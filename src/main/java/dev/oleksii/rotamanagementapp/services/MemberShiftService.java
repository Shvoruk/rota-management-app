package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;

import java.util.UUID;

public interface MemberShiftService {
    void deleteMemberShiftByShiftIdAndId(UUID shiftId, UUID memberShiftId);
    void saveMemberShift (MemberShift memberShift);
}