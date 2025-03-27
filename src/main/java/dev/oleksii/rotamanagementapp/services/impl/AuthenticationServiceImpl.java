package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.CreateUserRequest;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.AccessDeniedException;
import dev.oleksii.rotamanagementapp.exceptions.ConflictException;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.security.JwtService;
import dev.oleksii.rotamanagementapp.services.AuthenticationService;
import dev.oleksii.rotamanagementapp.services.UserService;
import dev.oleksii.rotamanagementapp.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link AuthenticationService} interface.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final VerificationService verificationService;

    /**
     * Registers a new user by:
     * <ul>
     *     <li>Checking if the given email is already in use</li>
     *     <li>Creating a new User using the {@link UserService}</li>
     *     <li>Sending a verification token to the newly registered user's email</li>
     * </ul>
     *
     * @param request A {@link CreateUserRequest} containing user registration details (e.g., email, password, full name).
     * @throws ConflictException if the email is already in use.
     */
    @Transactional
    @Override
    public void register(CreateUserRequest request) {
        // Check if the email is already taken
        if (repository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email is already in use.");
        }

        // Create a new User entity and persist it
        User user = userService.createUser(request);

        // Send a verification email/token to the newly created user
        verificationService.sendVerificationToken(user);
    }

    /**
     * Authenticates an existing user by:
     * <ul>
     *     <li>Verifying the user exists in the system</li>
     *     <li>Ensuring the user is email-verified</li>
     *     <li>Authenticating the provided credentials via {@link AuthenticationManager}</li>
     *     <li>Generating a JWT if authentication is successful</li>
     * </ul>
     *
     * @param request An {@link AuthenticationRequest} containing credentials (email and password).
     * @return An {@link AuthenticationResponse} containing a JWT token for the authenticated user.
     * @throws NotFoundException if the user is not found.
     * @throws AccessDeniedException if the user has not verified their email.
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Look up the user by email, or throw if not found
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found."));

        // Verify the user's email has been confirmed before login
        if (!user.isVerified()) {
            throw new AccessDeniedException("Please verify your email before logging in.");
        }

        // Perform the actual authentication check
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Generate a JWT token for the authenticated user
        var jwtToken = jwtService.generateToken(user);

        // Build and return the authentication response with the JWT
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
