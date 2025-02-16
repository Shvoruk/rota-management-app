package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;
import dev.oleksii.rotamanagementapp.mappers.MemberShiftMapper;
import dev.oleksii.rotamanagementapp.services.MemberShiftService;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/shifts/{shiftId}")
public class MemberShiftController {

    private final MemberShiftService memberShiftService;
    private final MemberShiftMapper memberShiftMapper;
    private final MembershipService membershipService;

    @PostMapping("/assign")
    public ResponseEntity<MemberShiftDto> assignShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            @Valid @RequestBody AssignShiftRequest request,
            Principal principal) {

        membershipService.checkManagerMembership(principal, teamId);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberShiftMapper.toMemberShiftDTO(memberShiftService.assignShift(shiftId, request)));
    }

    @DeleteMapping("/unassign/{memberShiftId}")
    public ResponseEntity<Void> unassignShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            @PathVariable UUID memberShiftId,
            Principal principal) {

        membershipService.checkManagerMembership(principal, teamId);
        memberShiftService.unassignShift(shiftId, memberShiftId);
        return ResponseEntity.noContent().build();
    }
}
