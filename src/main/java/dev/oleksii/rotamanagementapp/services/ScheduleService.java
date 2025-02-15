package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.entities.Team;

import java.util.UUID;

public interface ScheduleService {
    void createSchedule(Team team);
    ScheduleDto getSchedule(UUID teamId);
}
