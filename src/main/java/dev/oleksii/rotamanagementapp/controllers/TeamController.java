package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.SuccessResponse;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.UserNotFoundException;
import dev.oleksii.rotamanagementapp.services.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final UserRepository userRepository;

    public TeamController(TeamService teamService, UserRepository userRepository) {
        this.teamService = teamService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse> createTeam(@RequestParam String name, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
        teamService.createTeam(user, name);
        return ResponseEntity.ok(new SuccessResponse("Team created successfully"));
    }

    @DeleteMapping("/delete/{teamId}")
    public ResponseEntity<SuccessResponse> deleteTeam(@PathVariable UUID teamId, Principal principal) {
        var user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new UserNotFoundException("User not found"));
        teamService.deleteTeam(user, teamId);
        return ResponseEntity.ok(new SuccessResponse("Team deleted successfully"));
    }
}
