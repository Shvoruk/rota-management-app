package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.TeamRole;
import dev.oleksii.rotamanagementapp.domain.repos.ScheduleRepository;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.AccessDeniedException;
import dev.oleksii.rotamanagementapp.exceptions.UserNotFoundException;
import dev.oleksii.rotamanagementapp.services.AuthorizationService;
import dev.oleksii.rotamanagementapp.services.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final MembershipService membershipService;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final ShiftRepository shiftRepository;

    @Override
    public User getCurrentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User with email: " + principal.getName() + " not found"));
    }
    /**
     * Ensures the user is a member of the team.
     * Throws an exception if the user is not a member.
     */
    @Override
    public void checkMembership(User user, UUID teamId) {
        membershipService.findMembership(user, teamId);
    }
    /**
     * Ensures the user is a manager of the team.
     * Throws an AccessDeniedException if the user is not a manager.
     */
    @Override
    public void checkManagerByTeamId(User user, UUID teamId) {
        var membership = membershipService.findMembership(user, teamId);
        if (!membership.getRole().equals(TeamRole.MANAGER)) {
            throw new AccessDeniedException("Only a team manager can perform this action.");
        }
    }

    @Override
    public void checkManagerByShiftId(User user, UUID shiftId) {
        UUID teamId = shiftRepository.findTeamIdByShiftId(shiftId);
        var membership = membershipService.findMembership(user, teamId);
        if (!membership.getRole().equals(TeamRole.MANAGER)) {
            throw new AccessDeniedException("Only a team manager can perform this action.");
        }
    }

    @Override
    public void checkManagerByScheduleId(User user, UUID scheduleId) {
        UUID teamId = scheduleRepository.findTeamIdByScheduleId(scheduleId);
        var membership = membershipService.findMembership(user, teamId);
        if (!membership.getRole().equals(TeamRole.MANAGER)) {
            throw new AccessDeniedException("Only a team manager can perform this action.");
        }
    }
}
