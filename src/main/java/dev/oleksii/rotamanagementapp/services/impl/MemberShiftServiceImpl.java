package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.MemberShift;
import dev.oleksii.rotamanagementapp.domain.repos.MemberShiftRepository;
import dev.oleksii.rotamanagementapp.domain.repos.MembershipRepository;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.exceptions.MembershipNotFoundException;
import dev.oleksii.rotamanagementapp.exceptions.ShiftNotFoundException;
import dev.oleksii.rotamanagementapp.mappers.MemberShiftMapper;
import dev.oleksii.rotamanagementapp.services.MemberShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberShiftServiceImpl implements MemberShiftService {

    private final ShiftRepository shiftRepository;
    private final MemberShiftRepository memberShiftRepository;
    private final MembershipRepository membershipRepository;
    private final MemberShiftMapper memberShiftMapper;

     public MemberShiftDto assignShift(AssignShiftRequest request){
        var shift = shiftRepository.findById(request.getShiftId()).orElseThrow(() -> new ShiftNotFoundException("Shift not found"));
        var member = membershipRepository.findById(request.getMemberId()).orElseThrow(() -> new MembershipNotFoundException("There is no such member in this team"));
        var memberShift = MemberShift.builder()
                .shift(shift)
                .member(member)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();
        shift.addMemberShift(memberShift);
        shiftRepository.save(shift);
        return memberShiftMapper.toMemberShiftDTO(memberShift);
    }

    public void unassignShift(UUID memberShiftId){
         var memberShift = memberShiftRepository.findById(memberShiftId).orElseThrow(() -> new ShiftNotFoundException("Shift not found"));
         var shift = memberShift.getShift();
         shift.removeMemberShift(memberShift);
         shiftRepository.save(shift);
    }
}
