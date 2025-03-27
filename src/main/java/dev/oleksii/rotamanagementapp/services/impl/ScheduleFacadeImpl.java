package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;
import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.*;
import dev.oleksii.rotamanagementapp.mappers.MemberShiftMapper;
import dev.oleksii.rotamanagementapp.mappers.ShiftMapper;
import dev.oleksii.rotamanagementapp.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleFacadeImpl implements ScheduleFacade {

    private final ShiftService shiftService;
    private final MembershipService membershipService;
    private final ScheduleService scheduleService;
    private final MemberShiftService memberShiftService;
    private final MemberShiftMapper memberShiftMapper;
    private final ShiftMapper shiftMapper;

    public Shift findShift(UUID teamId, UUID shiftId) {
        return shiftService.getShiftByTeamIdAndShiftId(teamId, shiftId);
    }

    @Override
    public ShiftDto getShift(UUID teamId, UUID shiftId) {
        var shift = findShift(teamId, shiftId);
        return shiftMapper.toShiftDTO(shift);
    }

    @Override
    public Set<MemberShiftDto> getAllMemberShifts(User user, UUID teamId) {
        var member = membershipService.getMembershipByUserIdAndTeamId(user.getId(), teamId);
        var memberShifts = new HashSet<>(member.getMemberShifts());
        return memberShiftMapper.toMemberShiftsDTO(memberShifts);
    }

    @Override
    public ShiftDto createShift(UUID teamId, CreateShiftRequest request) {
        var schedule = scheduleService.getScheduleByTeamId(teamId);
        Shift shift = Shift.builder()
                .name(request.getName())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        schedule.addShift(shift);
        shiftService.saveShift(shift);
        return shiftMapper.toShiftDTO(shift);
    }

    @Override
    public void deleteShift(UUID teamId, UUID shiftId) {
        var shift = findShift(teamId, shiftId);
        shiftService.deleteShiftById(shift.getId());
    }

    @Override
    public MemberShiftDto assignShift(UUID teamId, UUID shiftId, AssignShiftRequest request) {
        var shift = findShift(teamId, shiftId);

        var member = membershipService.getMembershipById(request.getMemberId());

        var memberShift = MemberShift.builder()
                .member(member)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        shift.addMemberShift(memberShift);
        memberShiftService.saveMemberShift(memberShift);
        return memberShiftMapper.toMemberShiftDTO(memberShift);
    }

    @Override
    @Transactional
    public void unassignShift(UUID teamId, UUID shiftId, UUID memberShiftId) {
        findShift(teamId, shiftId);
        memberShiftService.deleteMemberShiftByShiftIdAndId(shiftId, memberShiftId);
    }
}
