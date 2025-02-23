package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import dev.oleksii.rotamanagementapp.domain.repos.MemberShiftRepository;
import dev.oleksii.rotamanagementapp.services.MemberShiftService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MemberShiftServiceImpl implements MemberShiftService {

    private final MemberShiftRepository memberShiftRepository;

    public MemberShiftServiceImpl(MemberShiftRepository memberShiftRepository) {
        this.memberShiftRepository = memberShiftRepository;
    }

    @Override
    public void saveMemberShift(MemberShift memberShift) {
        memberShiftRepository.save(memberShift);
    }
    @Override
    public void deleteMemberShiftByShiftIdAndId(UUID shiftId, UUID memberShiftId) {
        memberShiftRepository.deleteByShiftIdAndId(shiftId, memberShiftId);
    }
}
