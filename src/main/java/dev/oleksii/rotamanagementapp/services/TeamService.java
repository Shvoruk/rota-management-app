package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.entities.User;

import java.util.UUID;

public interface TeamService {

    void createTeam(User user, String name);
    void deleteTeam(User user, UUID teamId);
}
