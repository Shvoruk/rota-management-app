package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateTeamRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.SuccessResponse;
import dev.oleksii.rotamanagementapp.services.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createTeam(@RequestBody CreateTeamRequest request, Principal principal) {
        teamService.createTeam(principal.getName(), request);
        return ResponseEntity.ok(new SuccessResponse("Team created successfully"));
    }
}
