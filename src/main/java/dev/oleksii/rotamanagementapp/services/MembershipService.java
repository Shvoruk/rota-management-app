package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.Member;
import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.security.Principal;
import java.util.UUID;

public interface MembershipService {
    void checkMembership(Principal principal, UUID teamId);
    User checkNoMembership(Principal principal, UUID teamId);
    void checkManagerMembership(Principal principal, UUID teamId);
    Member getMembership(Principal principal, UUID teamId);
}
