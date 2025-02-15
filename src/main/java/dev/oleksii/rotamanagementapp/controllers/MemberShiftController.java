package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.AssignShiftRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberShiftDto;
import dev.oleksii.rotamanagementapp.services.MemberShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member-shifts")
public class MemberShiftController {

    private final MemberShiftService memberShiftService;

    @PostMapping
    public ResponseEntity<MemberShiftDto> assignShift(@Valid @RequestBody AssignShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberShiftService.assignShift(request));
    }

    @DeleteMapping("/{memberShiftId}")
    public ResponseEntity<Void> unassignShift(@PathVariable UUID memberShiftId) {
        memberShiftService.unassignShift(memberShiftId);
        return ResponseEntity.noContent().build();
    }
}
