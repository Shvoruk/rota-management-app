package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;

/**
 * Service interface that defines the contract for user email verification-related operations.
 */
public interface VerificationService {

    /**
     * Creates a new verification token for the given user by:
     * <ul>
     *   <li>Generating a new unique token using a random UUID.</li>
     *   <li>Setting the token's expiration date based on the configured duration.</li>
     *   <li>Attaching the newly created token to the user and saving the updated user entity.</li>
     *   <li>Returning the created {@link VerificationToken}.</li>
     * </ul>
     *
     * @param user The user who needs a verification token.
     * @return The created {@link VerificationToken} object.
     */
    VerificationToken createVerificationToken(User user);

    /**
     * Validates the provided verification token and, if valid, marks the associated user as verified by:
     * <ul>
     *   <li>Retrieving the user by the embedded verification token.</li>
     *   <li>Checking if the token is expired; if so, throwing an appropriate exception.</li>
     *   <li>Marking the user as verified and clearing the embedded verification token.</li>
     *   <li>Saving the updated user entity.</li>
     *   <li>Generating a JWT for the now verified user and returning it within an {@link AuthenticationResponse}.</li>
     * </ul>
     *
     * @param token The verification token to be verified.
     * @return An {@link AuthenticationResponse} containing a new JWT for the verified user.
     */
    AuthenticationResponse verify(String token);

    /**
     * Resends a verification token to the specified user's email by:
     * <ul>
     *   <li>Retrieving the user by their email address.</li>
     *   <li>Throwing an exception if the user is already verified.</li>
     *   <li>Creating a new verification token for the user.</li>
     *   <li>Sending the verification token via email to the user.</li>
     * </ul>
     *
     * @param email The email address of the user who needs the verification token resent.
     */
    void resendVerificationToken(String email);

    /**
     * Sends an existing verification token to the user via email by:
     * <ul>
     *   <li>Retrieving the user's embedded verification token.</li>
     *   <li>Constructing a verification link using the token and the configured base URL.</li>
     *   <li>Sending the verification email to the user.</li>
     * </ul>
     *
     * @param user The user who will receive the verification token email.
     */
    void sendVerificationToken(User user);

    /**
     * Generates a new random string that serves as a verification token
     *
     * @return The newly generated verification token string.
     */
    String generateVerificationToken();
}
