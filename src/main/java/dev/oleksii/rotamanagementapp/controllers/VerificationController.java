package dev.oleksii.rotamanagementapp.controllers;


import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;

import dev.oleksii.rotamanagementapp.domain.dtos.SuccessResponse;
import dev.oleksii.rotamanagementapp.services.EmailVerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/auth")
public class VerificationController {

    private final EmailVerificationService emailVerificationService;

    public VerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/verify")
    public ResponseEntity<AuthenticationResponse> verify(@RequestParam String token) {
        return ResponseEntity.ok(emailVerificationService.verify(token));
    }

    @GetMapping("/resend-verification")
    public ResponseEntity<SuccessResponse> resendVerificationToken(@RequestParam String email) {
        emailVerificationService.resendVerification(email);
        return ResponseEntity.ok(new SuccessResponse("Please check your email"));
    }
}