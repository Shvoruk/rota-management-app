package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Schedule;

import java.util.UUID;

public interface ScheduleService {
    Schedule getSchedule(UUID teamId);
}
