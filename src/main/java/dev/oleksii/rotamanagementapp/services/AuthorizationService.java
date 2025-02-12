package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.security.Principal;
import java.util.UUID;

public interface AuthorizationService {
    User getCurrentUser(Principal principal);
    void checkMembership(User user, UUID teamId);
    void checkManagerByTeamId(User user, UUID teamId);
    void checkManagerByShiftId(User user, UUID shiftId);
    void checkManagerByScheduleId(User user, UUID scheduleId);
}
