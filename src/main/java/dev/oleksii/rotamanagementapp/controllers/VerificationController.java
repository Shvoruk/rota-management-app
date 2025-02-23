package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller responsible for handling email verification operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/verify")
public class VerificationController {

    private final VerificationService verificationService;

    /**
     * Verifies the user's email address using the provided verification token.
     * <p>
     * When a user clicks the verification link sent via email, this endpoint is triggered.
     * It validates the token, ensures that it belongs to the currently authenticated user,
     * marks the user as verified, and returns an {@link AuthenticationResponse} containing a new JWT.
     *
     * @param verificationToken the verification token included in the email link.
     * @return a ResponseEntity containing the AuthenticationResponse with a new JWT.
     */
    @GetMapping
    public ResponseEntity<AuthenticationResponse> verify(@RequestParam String verificationToken) {
        return ResponseEntity.ok(
                verificationService.verify(verificationToken)
        );
    }

    /**
     * Resends the verification token to the user's email.
     * <p>
     * If a user hasn't received or has lost the initial verification token, they can trigger this endpoint
     * to have a new token sent to their email address.
     *
     * @param email the email address to which the verification token will be resent.
     * @return a ResponseEntity with a confirmation message instructing the user to check their email.
     */
    @GetMapping("/resend")
    public ResponseEntity<String> resendVerificationToken(@RequestParam String email) {
        verificationService.resendVerificationToken(email);
        return ResponseEntity.ok("Please check your email.");
    }
}
