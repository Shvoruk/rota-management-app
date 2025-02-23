package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.*;
import dev.oleksii.rotamanagementapp.exceptions.AccessDeniedException;
import dev.oleksii.rotamanagementapp.security.SecurityUtil;
import dev.oleksii.rotamanagementapp.services.ScheduleFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams/{teamId}/shifts")
public class ScheduleController {

    // Facade service that handles all schedule related business logic.
    private final ScheduleFacade scheduleFacade;
    // Utility to extract the current user and check their permissions.
    private final SecurityUtil securityUtil;

    /**
     * Endpoint to retrieve all member shifts for a given team.
     *
     * @param teamId the identifier of the team whose shifts are requested.
     * @param principal the currently authenticated user.
     * @return a ResponseEntity containing a set of MemberShiftDto objects.
     */
    @GetMapping
    public ResponseEntity<Set<MemberShiftDto>> getAllMemberShifts(
            @PathVariable UUID teamId,
            Principal principal) {

        // Get the current user from the security context.
        var user = securityUtil.getCurrentUser(principal);
        // Check if the user is a member of the team; if not, throw access denied exception.
        if (!securityUtil.isMember(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to view these shifts.");
        }
        // Fetch and return all shifts assigned to team members.
        return ResponseEntity.ok(
                scheduleFacade.getAllMemberShifts(user, teamId)
        );
    }

    /**
     * Endpoint to retrieve a specific shift from a team.
     *
     * @param teamId the identifier of the team.
     * @param shiftId the identifier of the shift.
     * @param principal the currently authenticated user.
     * @return a ResponseEntity containing a ShiftDto with shift details.
     */
    @GetMapping("/{shiftId}")
    public ResponseEntity<ShiftDto> getShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            Principal principal) {

        // Get the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Ensure the user is a member of the team before allowing access.
        if (!securityUtil.isMember(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to view this shift.");
        }
        // Retrieve and return the specified shift.
        return ResponseEntity.ok(scheduleFacade.getShift(teamId, shiftId));
    }

    /**
     * Endpoint to create a new shift for a team.
     *
     * @param teamId the identifier of the team.
     * @param request the request body containing details needed to create a shift.
     * @param principal the currently authenticated user.
     * @return a ResponseEntity containing the created ShiftDto.
     */
    @PostMapping
    public ResponseEntity<ShiftDto> createShift(
            @PathVariable UUID teamId,
            @Valid @RequestBody CreateShiftRequest request,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Deny access if the user is not a manager.
        if (!securityUtil.isManager(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to create shifts.");
        }
        // Delegate shift creation to the service and return the new shift with HTTP 201 status.
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleFacade.createShift(teamId, request));
    }

    /**
     * Endpoint to delete an existing shift from a team.
     *
     * @param teamId the identifier of the team.
     * @param shiftId the identifier of the shift to be deleted.
     * @param principal the currently authenticated user.
     * @return a ResponseEntity containing a success message.
     */
    @DeleteMapping("/{shiftId}")
    public ResponseEntity<String> deleteShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Deny deletion if the user is not a manager.
        if (!securityUtil.isManager(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to delete shifts.");
        }
        // Perform the deletion operation.
        scheduleFacade.deleteShift(teamId, shiftId);
        return ResponseEntity.ok("Shift deleted successfully.");
    }

    /**
     * Endpoint to assign a shift to a member.
     *
     * @param teamId the identifier of the team.
     * @param shiftId the identifier of the shift.
     * @param request the request body containing assignment details.
     * @param principal the currently authenticated user.
     * @return a ResponseEntity containing the updated MemberShiftDto.
     */
    @PostMapping("/{shiftId}/assign")
    public ResponseEntity<MemberShiftDto> assignShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            @Valid @RequestBody AssignShiftRequest request,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Check permission
        if (!securityUtil.isManager(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to assign shifts.");
        }
        // Delegate the assignment to the service and return the result with HTTP 201 status.
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleFacade.assignShift(teamId, shiftId, request));
    }

    /**
     * Endpoint to unassign a shift from a member.
     *
     * @param teamId the identifier of the team.
     * @param shiftId the identifier of the shift.
     * @param memberShiftId the identifier of the member's shift assignment to remove.
     * @param principal the currently authenticated user.
     * @return a ResponseEntity containing a success message.
     */
    @DeleteMapping("/{shiftId}/unassign/{memberShiftId}")
    public ResponseEntity<String> unassignShift(
            @PathVariable UUID teamId,
            @PathVariable UUID shiftId,
            @PathVariable UUID memberShiftId,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Check if the user is permitted to unassign shifts.
        if (!securityUtil.isManager(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to unassign shifts.");
        }
        // Delegate the unassignment operation to the service.
        scheduleFacade.unassignShift(teamId, shiftId, memberShiftId);
        return ResponseEntity.ok("shift unassigned successfully.");
    }
}
