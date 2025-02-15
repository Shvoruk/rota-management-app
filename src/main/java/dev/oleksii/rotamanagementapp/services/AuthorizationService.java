package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.security.Principal;

public interface AuthorizationService {
    User getCurrentUser(Principal principal);
}
