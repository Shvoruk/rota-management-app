package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.ScheduleDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.exceptions.AccessDeniedException;
import dev.oleksii.rotamanagementapp.security.SecurityUtil;
import dev.oleksii.rotamanagementapp.services.TeamFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

/**
 * REST controller for handling team-related operations.
 * Key aspects:
 * - Uses a facade (TeamFacade) to delegate team operations.
 * - Retrieves the current user via SecurityUtil and Principal to enforce security.
 * - Contains endpoints for viewing, creating, joining, leaving, and deleting teams,
 *   as well as for retrieving a team's schedule and members.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamFacade teamFacade;
    // Utility to extract the current user and perform role-based checks.
    private final SecurityUtil securityUtil;

    /**
     * Retrieves all teams the currently authenticated user is member of.
     *
     * @param principal The security principal of the current user.
     * @return ResponseEntity containing a set of TeamDto objects.
     */
    @GetMapping
    public ResponseEntity<Set<TeamDto>> getAllTeams(Principal principal) {
        // Retrieve the current authenticated user.
        var user = securityUtil.getCurrentUser(principal);
        // Delegate fetching teams to the teamFacade and return them with HTTP 200 OK.
        return ResponseEntity.ok(teamFacade.getAllTeams(user));
    }

    /**
     * Retrieves the schedule for a specific team.
     *
     * @param teamId    The UUID of the team whose schedule is requested.
     * @param principal The security principal of the current user.
     * @return ResponseEntity containing the ScheduleDto.
     * @throws AccessDeniedException if the user is not a member of the team.
     */
    @GetMapping("/{teamId}/schedule")
    public ResponseEntity<ScheduleDto> getTeamSchedule(
            @PathVariable UUID teamId,
            Principal principal) {

        // Get the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Check if the user is a member of the requested team; if not, deny access.
        if (!securityUtil.isMember(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to view team schedule.");
        }
        // Retrieve and return the team schedule.
        return ResponseEntity.ok(teamFacade.getTeamSchedule(teamId));
    }

    /**
     * Retrieves details of a specific team.
     *
     * @param teamId    The UUID of the team to retrieve.
     * @param principal The security principal of the current user.
     * @return ResponseEntity containing the TeamDto.
     * @throws AccessDeniedException if the user is not a member of the team.
     */
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDto> getTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        // Get the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Verify membership before returning team details.
        if (!securityUtil.isMember(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to view team.");
        }
        // Return the team details.
        return ResponseEntity.ok(teamFacade.getTeam(teamId));
    }

    /**
     * Creates a new team with the given name.
     *
     * @param name      The name of the team to be created.
     * @param principal The security principal of the current user.
     * @return ResponseEntity containing the newly created TeamDto.
     */
    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @RequestParam String name,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Create a new team using the teamFacade.
        var team = teamFacade.createTeam(user, name);
        return ResponseEntity.status(HttpStatus.CREATED).body(team);
    }

    /**
     * Allows the current user to join a team.
     *
     * @param teamId    The UUID of the team to join.
     * @param principal The security principal of the current user.
     * @return ResponseEntity containing the updated TeamDto.
     * @throws AccessDeniedException if the user is already a member of the team.
     */
    @PostMapping("/{teamId}/join")
    public ResponseEntity<TeamDto> joinTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        // Get the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Prevent joining if already a team member.
        if (securityUtil.isMember(user.getId(), teamId)) {
            throw new AccessDeniedException("User is already a member of this team.");
        }
        // Process the join operation and return the updated team details.
        return ResponseEntity.ok(teamFacade.joinTeam(user, teamId));
    }

    /**
     * Allows the current user to leave a team.
     *
     * @param teamId    The UUID of the team to leave.
     * @param principal The security principal of the current user.
     * @return ResponseEntity with a success message.
     */
    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<String> leaveTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Delegate the leave operation to the teamFacade.
        teamFacade.leaveTeam(user, teamId);
        return ResponseEntity.ok("Leaved team successfully.");
    }

    /**
     * Deletes a team.
     *
     * @param teamId    The UUID of the team to delete.
     * @param principal The security principal of the current user.
     * @return ResponseEntity with a success message.
     * @throws AccessDeniedException if the user does not have the required permission.
     */
    @DeleteMapping("/{teamId}")
    public ResponseEntity<String> deleteTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        // Retrieve the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Check if the user is a manager, which in this case prevents deletion.
        if (!securityUtil.isManager(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to delete team.");
        }
        // Delegate the deletion operation to the teamFacade.
        teamFacade.deleteTeam(teamId);
        // Return a success message.
        return ResponseEntity.ok("Deleted team successfully.");
    }

    /**
     * Retrieves all members of a specific team.
     *
     * @param teamId    The UUID of the team.
     * @param principal The security principal of the current user.
     * @return ResponseEntity containing a set of MemberDto objects.
     * @throws AccessDeniedException if the user is not a member of the team.
     */
    @GetMapping("/{teamId}/members")
    public ResponseEntity<Set<MemberDto>> getAllTeamMembers(
            @PathVariable UUID teamId,
            Principal principal) {

        // Get the current user.
        var user = securityUtil.getCurrentUser(principal);
        // Verify that the user is a member before showing team members.
        if (!securityUtil.isMember(user.getId(), teamId)) {
            throw new AccessDeniedException("User doesn't have permission to view members.");
        }
        // Retrieve and return the set of team members.
        return ResponseEntity.ok(teamFacade.getAllTeamMembers(teamId));
    }
}
