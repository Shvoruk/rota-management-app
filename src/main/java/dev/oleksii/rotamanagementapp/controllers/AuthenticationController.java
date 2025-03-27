package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.CreateUserRequest;
import dev.oleksii.rotamanagementapp.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling authentication operations
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint for registering a new user.
     * HTTP Method: POST
     * URL: /api/v1/auth/register
     *
     * @param request A validated CreateUserRequest containing user registration details.
     * @return ResponseEntity with status 201 and a confirmation message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody CreateUserRequest request) {
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }

    /**
     * Endpoint for authenticating a user.
     * HTTP Method: POST
     * URL: /api/v1/auth/login
     *
     * @param request A validated AuthenticationRequest containing login credentials.
     * @return ResponseEntity with status 200 and an AuthenticationResponse containing JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
