package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.exceptions.ScheduleNotFoundException;
import dev.oleksii.rotamanagementapp.mappers.ScheduleMapper;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public ScheduleDto getSchedule(UUID teamId) {
        var schedule = scheduleRepository.findByTeamId(teamId).orElseThrow(() -> new ScheduleNotFoundException("Schedule not found"));
        return scheduleMapper.toScheduleDTO(schedule);
    }
}
