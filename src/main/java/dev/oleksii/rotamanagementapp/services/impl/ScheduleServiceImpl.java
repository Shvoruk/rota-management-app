package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.ScheduleService;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule getScheduleByTeamId(UUID teamId) {
        return scheduleRepository.findByTeamId(teamId)
                .orElseThrow(() -> new NotFoundException("No schedule found for team with ID " + teamId));
    }
}
