package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.MemberDto;
import dev.oleksii.rotamanagementapp.domain.dtos.TeamDto;
import dev.oleksii.rotamanagementapp.security.SecurityUtil;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import dev.oleksii.rotamanagementapp.services.TeamService;
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
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final MembershipService membershipService;
    private final SecurityUtil securityUtil;

    @GetMapping
    public ResponseEntity<Set<TeamDto>> getAllTeams(Principal principal) {
        var user = securityUtil.getCurrentUser(principal);
        return ResponseEntity.ok(teamService.getAllTeams(user));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamDto> getTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        membershipService.checkMembership(principal, teamId);
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @Valid @RequestBody CreateTeamRequest request,
            Principal principal) {

        var user = securityUtil.getCurrentUser(principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.createTeam(user, request));
    }

    @PostMapping("/{teamId}/join")
    public ResponseEntity<TeamDto> joinTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        var user = membershipService.checkNoMembership(principal, teamId);
        return ResponseEntity.ok(teamService.joinTeam(user, teamId));
    }

    @DeleteMapping("/{teamId}/members")
    public ResponseEntity<Void> leaveTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        var member = membershipService.getMembership(principal, teamId);
        teamService.leaveTeam(member, teamId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Void> deleteTeam(
            @PathVariable UUID teamId,
            Principal principal) {

        membershipService.checkManagerMembership(principal, teamId);
        teamService.deleteTeam(teamId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/members")
    public ResponseEntity<Set<MemberDto>> getAllTeamMembers(
            @PathVariable UUID teamId,
            Principal principal) {

        membershipService.checkMembership(principal, teamId);
        return ResponseEntity.ok(teamService.getAllTeamMembers(teamId));
    }

}
