package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.configuration.VerificationConfig;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;
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
 * Implementation of the {@link VerificationService} interface.
 */
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationConfig verificationConfig;
    private final EmailService emailService;

    /**
     * Verifies the user's email using the provided token by:
     * <ul>
     *   <li>Retrieving the user by the embedded verification token.</li>
     *   <li>Checking if the token is expired; if so, throws a {@link TokenExpiredException}.</li>
     *   <li>Marking the user as verified and clearing the embedded verification token.</li>
     *   <li>Saving the updated user entity.</li>
     *   <li>Generating a JWT for the now verified user and returning it within an {@link AuthenticationResponse}.</li>
     * </ul>
     *
     * @param token The verification token provided by the user.
     * @return An {@link AuthenticationResponse} containing a JWT for the verified user.
     * @throws NotFoundException if no user is found with the provided token.
     * @throws TokenExpiredException if the verification token has expired.
     */

    @Override
    @Transactional
    public AuthenticationResponse verify(String token) {
        var user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (user.getVerificationToken().getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Expired verification token.");
        }

        // Mark user as verified and clear the embedded token.
        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Creates (or replaces) the embedded verification token for the specified user by:
     * <ul>
     *   <li>Generating a new unique token using a random UUID.</li>
     *   <li>Setting the token's expiration date based on the configured duration.</li>
     *   <li>Attaching the newly created token to the user and saving the updated user entity.</li>
     *   <li>Returning the created {@link VerificationToken}.</li>
     * </ul>
     *
     * @param user The user for whom the verification token is being created.
     * @return The newly created {@link VerificationToken}.
     */
    @Override
    @Transactional
    public VerificationToken createVerificationToken(User user) {
        var verificationToken = VerificationToken.builder()
                .token(generateVerificationToken())
                .expirationDate(LocalDateTime.now().plusMinutes(verificationConfig.getTokenExpirationMinutes()))
                .build();

        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        return verificationToken;
    }

    /**
     * Resends the verification token email by:
     * <ul>
     *   <li>Retrieving the user by their email address.</li>
     *   <li>Throwing a {@link ConflictException} if the user is already verified.</li>
     *   <li>Creating a new verification token for the user.</li>
     *   <li>Sending the verification token via email to the user.</li>
     * </ul>
     *
     * @param email The email address of the user for whom the verification token should be resent.
     * @throws NotFoundException if no user is found with the provided email.
     * @throws ConflictException if the user is already verified.
     */
    @Override
    @Transactional
    public void resendVerificationToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " not found."));
        if (user.isVerified()) {
            throw new ConflictException("User with email " + email + " is already verified.");
        }
        createVerificationToken(user);
        sendVerificationToken(user);
    }

    /**
     * Sends the verification token to the specified user's email by:
     * <ul>
     *   <li>Retrieving the user's embedded verification token.</li>
     *   <li>Throwing a {@link NotFoundException} if no token is found.</li>
     *   <li>Constructing a verification link using the token and the configured base URL.</li>
     *   <li>Sending the verification email using the {@link EmailService}.</li>
     * </ul>
     *
     * @param user The user to whom the verification token email should be sent.
     * @throws NotFoundException if the user does not have a verification token.
     */
    @Override
    @Transactional
    public void sendVerificationToken(User user) {
        var verificationToken = user.getVerificationToken();
        if (verificationToken == null) {
            throw new NotFoundException("No verification token found for user.");
        }
        String link = verificationConfig.getVerificationLink() + verificationToken.getToken();
        emailService.sendVerificationEmail(user.getEmail(), "Verify your email", "verify-email", link);
    }

    /**
     * Generates a unique verification token string
     *
     * @return A unique verification token as a {@link String}.
     */
    @Override
    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
