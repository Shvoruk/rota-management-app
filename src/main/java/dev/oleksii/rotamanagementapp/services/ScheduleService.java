package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;

import java.util.UUID;

public interface ScheduleService {
    ScheduleDto getSchedule(UUID teamId);
}
