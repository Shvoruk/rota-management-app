package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.configuration.VerificationConfig;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;
import dev.oleksii.rotamanagementapp.domain.repos.TokenRepository;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.TokenExpiredException;
import dev.oleksii.rotamanagementapp.exceptions.ConflictException;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.security.JwtService;
import dev.oleksii.rotamanagementapp.services.EmailService;
import dev.oleksii.rotamanagementapp.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service implementation for user email verification flow.
 * Handles verification token creation, email sending, and token validation.
 */
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationConfig verificationConfig;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    /**
     * Validates the provided verification token.
     * - Checks if the token exists
     * - Checks if the token has expired
     * - Verifies the user (sets user.verified = true)
     * - Deletes the token once used
     * - Generates and returns a new JWT for the user
     *
     * @param token The verification token to validate
     * @return AuthenticationResponse containing the new JWT
     */
    @Override
    @Transactional
    public AuthenticationResponse verify(String token) {
        // Look up the token in the database or throw if not found
        var verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token."));

        // Check if the token has expired
        if (verificationToken.getExpirationDate() != null
                && verificationToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Expired verification token.");
        }

        // Mark the user as verified and save
        var user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);

        // Since the token is only supposed to be used once, remove it from the DB
        tokenRepository.delete(verificationToken);

        // Generate a fresh JWT for the now-verified user
        var jwtToken = jwtService.generateToken(user);

        // Return the new JWT in an authentication response
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Creates a new VerificationToken object for a given user.
     * - Generates a random token string
     * - Calculates the expiration date/time
     * - Persists the token to the database
     *
     * @param user The user who needs verification
     * @return The newly created VerificationToken
     */
    @Override
    @Transactional
    public VerificationToken createVerificationToken(User user) {
        // Build a new VerificationToken instance
        var verificationToken = VerificationToken.builder()
                .token(generateVerificationToken())  // random token string
                .expirationDate(LocalDateTime.now().plusMinutes(verificationConfig.getTokenExpirationMinutes())) // set expiration
                .user(user)
                .build();

        // Save the token in the repository
        tokenRepository.save(verificationToken);
        return verificationToken;
    }

    /**
     * Resends a verification token email if the user is not yet verified.
     * - Looks up user by email
     * - Throws if user is already verified
     * - Creates a new token
     * - Sends the token to the user by email
     *
     * @param email The user's email
     */
    @Override
    @Transactional
    public void resendVerificationToken(String email) {
        // Fetch user by email or throw if not found
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found."));

        // If the user is already verified, no need to re-send a token
        if (user.isVerified()) {
            throw new ConflictException("User with email " + email + " is already verified.");
        }

        // Create a fresh token for this user
        tokenRepository.save(createVerificationToken(user));

        // Send the token to the user by email
        sendVerificationToken(user);
    }

    /**
     * Sends an existing verification token to the user via email.
     * - Retrieves the user's token
     * - Builds the verification URL
     * - Sends an email using the EmailService
     *
     * @param user The user to receive the token
     */
    @Override
    @Transactional
    public void sendVerificationToken(User user) {
        // Retrieve the verification token for the user
        var verificationToken = tokenRepository.findByUser(user)
                .orElseThrow();

        // Construct the full verification link using config + token
        String link = verificationConfig.getVerificationLink() + verificationToken.getToken();

        // Send the verification email
        emailService.sendEmail(user.getEmail(), "Verify your email", link);
    }

    /**
     * Generates a random verification token string (UUID).
     *
     * @return Random token string
     */
    @Override
    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
