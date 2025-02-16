package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.ShiftDto;
import dev.oleksii.rotamanagementapp.domain.entities.*;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.exceptions.ScheduleNotFoundException;
import dev.oleksii.rotamanagementapp.exceptions.ShiftNotFoundException;
import dev.oleksii.rotamanagementapp.mappers.ShiftMapper;
import dev.oleksii.rotamanagementapp.services.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;
    private final ScheduleRepository scheduleRepository;
    private final ShiftMapper shiftMapper;

    @Transactional
    @Override
    public ShiftDto createShift(CreateShiftRequest request) {
        var schedule = scheduleRepository.findById(request.getScheduleId()).orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
        var shift = Shift.builder()
                .name(request.getName())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .schedule(schedule)
                .build();

        shiftRepository.save(shift);
        return shiftMapper.toShiftDTO(shift);
    }

    @Override
    public ShiftDto getShift(UUID shiftId) {
        var shift = shiftRepository.findById(shiftId).orElseThrow(() -> new ShiftNotFoundException("Shift not found"));
        return shiftMapper.toShiftDTO(shift);
    }

    @Override
    public void deleteShift(UUID shiftId) {
        shiftRepository.deleteById(shiftId);
    }

}
