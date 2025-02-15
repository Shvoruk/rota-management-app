package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.*;
import dev.oleksii.rotamanagementapp.services.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftDto> createShift(@Valid @RequestBody CreateShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.createShift(request));
    }

    @PostMapping("/assign")
    public ResponseEntity<ShiftDto> assignShift(@Valid @RequestBody AssignShiftRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(shiftService.assignShift(request));
    }

    @DeleteMapping("/unassign/{memberShiftId}")
    public ResponseEntity<Void> unassignShift(@PathVariable UUID memberShiftId) {
        shiftService.unassignShift(memberShiftId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{shiftId}")
    public ResponseEntity<Void> deleteShift(@PathVariable UUID shiftId) {
        shiftService.deleteShift(shiftId);
        return ResponseEntity.noContent().build();
    }

}