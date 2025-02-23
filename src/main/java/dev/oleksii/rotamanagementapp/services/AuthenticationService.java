package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.CreateUserRequest;

/**
 * Service interface defining the contract for user authentication and registration processes.
 */
public interface AuthenticationService {

    /**
     * Registers a new user based on the provided CreateUserRequest.
     *
     * @param request DTO containing the information needed to register a user (e.g., email, password).
     */
    void register(CreateUserRequest request);

    /**
     * Authenticates a user with the provided credentials.
     * If successful, returns an AuthenticationResponse that includes jwt token.
     *
     * @param request DTO containing login credentials (email, password).
     * @return An AuthenticationResponse containing JWT.
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
