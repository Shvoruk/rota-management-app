package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.*;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import dev.oleksii.rotamanagementapp.services.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/shifts")
public class ShiftController {

    private final ShiftService shiftService;
    private final MembershipService membershipService;

    @GetMapping("/{shiftId}")
    public ResponseEntity<ShiftDto> getShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            Principal principal) {

        membershipService.checkMembership(principal, teamId);
        return ResponseEntity.ok(shiftService.getShiftDto(shiftId));
    }

    @PostMapping
    public ResponseEntity<ShiftDto> createShift(
            @PathVariable UUID teamId,
            @Valid @RequestBody CreateShiftRequest request,
            Principal principal) {

        membershipService.checkManagerMembership(principal, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.createShift(request));
    }

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            Principal principal) {

        membershipService.checkManagerMembership(principal, teamId);
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }

}