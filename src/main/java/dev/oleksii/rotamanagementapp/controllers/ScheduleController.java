package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.mappers.ScheduleMapper;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import dev.oleksii.rotamanagementapp.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleMapper scheduleMapper;
    private final MembershipService membershipService;

    @GetMapping
    public ResponseEntity<ScheduleDto> getSchedule(
            @PathVariable UUID teamId,
            Principal principal) {

        membershipService.checkMembership(principal, teamId);
        return ResponseEntity.ok(scheduleMapper.toScheduleDTO(scheduleService.getSchedule(teamId)));
    }

}
