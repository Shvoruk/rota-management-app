package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import dev.oleksii.rotamanagementapp.domain.repos.MemberShiftRepository;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.MemberShiftService;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import dev.oleksii.rotamanagementapp.services.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberShiftServiceImpl implements MemberShiftService {

    private final ShiftService shiftService;
    private final ShiftRepository shiftRepository;
    private final MemberShiftRepository memberShiftRepository;
    private final MembershipService membershipService;

     public MemberShift assignShift(UUID shiftId, AssignShiftRequest request){
        var shift = shiftService.getShift(shiftId);
        var member = membershipService.getMembership(request.getMemberId());
        var memberShift = MemberShift.builder()
                .shift(shift)
                .member(member)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        shift.addMemberShift(memberShift);
        shiftRepository.save(shift);
        return memberShift;
    }

    public void unassignShift(UUID shiftId, UUID memberShiftId){
         var memberShift = memberShiftRepository.findById(memberShiftId).orElseThrow(() -> new NotFoundException("Shift not found"));
         var shift = memberShift.getShift();
         if (!shift.getId().equals(shiftId)) {
            throw new NotFoundException("No shift found with Id " + shiftId + " for member with Id " + memberShiftId);
         }
         shift.removeMemberShift(memberShift);
         shiftRepository.save(shift);
    }
}
