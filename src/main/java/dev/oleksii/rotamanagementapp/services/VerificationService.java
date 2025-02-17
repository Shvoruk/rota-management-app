package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;

/**
 * Service interface that defines the contract for user email verification-related operations.
 */
public interface VerificationService {

    /**
     * Creates a new verification token for the given user.
     *
     * @param user The user who needs a verification token.
     * @return The created VerificationToken object.
     */
    VerificationToken createVerificationToken(User user);

    /**
     * Validates the provided verification token and, if valid, marks the associated user as verified.
     *
     * @param token The token to be verified.
     * @return An AuthenticationResponse containing a new JWT if verification is successful.
     */
    AuthenticationResponse verify(String token);

    /**
     * Resends a verification token to the specified user's email if the user isn't already verified.
     *
     * @param email The email of the user who needs a token resent.
     */
    void resendVerificationToken(String email);

    /**
     * Sends an existing verification token to the user via email.
     *
     * @param user The user who will receive the verification token email.
     */
    void sendVerificationToken(User user);

    /**
     * Generates a new random string that serves as a verification token (e.g., a UUID).
     *
     * @return The newly generated verification token string.
     */
    String generateVerificationToken();
}
