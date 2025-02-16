package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.entities.*;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import dev.oleksii.rotamanagementapp.services.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ScheduleService scheduleService;

    public Shift findShiftById(UUID teamId, UUID shiftId) {
        var schedule = scheduleService.getSchedule(teamId);
        var shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift not found"));
        if(!shift.getSchedule().equals(schedule)){
            throw new NotFoundException("Shift not found");
        }
        return shift;
    }

    @Transactional
    @Override
    public Shift createShift(UUID teamId, CreateShiftRequest request) {
        var schedule = scheduleService.getSchedule(teamId);
        var shift = Shift.builder()
                .name(request.getName())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .schedule(schedule)
                .build();

        shiftRepository.save(shift);
        return shift;
    }

    @Override
    public void deleteShift(UUID teamId, UUID shiftId) {
        shiftRepository.delete(findShiftById(teamId, shiftId));
    }

    @Override
    public Shift getShift(UUID shiftId) {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new NotFoundException("Shift not found"));
    }

    @Override
    public Shift getShift(UUID teamId, UUID shiftId) {
        return findShiftById(teamId, shiftId);
    }

}
