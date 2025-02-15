package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/{teamId}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable UUID teamId) {
        return ResponseEntity.ok(scheduleService.getSchedule(teamId));
    }

}
