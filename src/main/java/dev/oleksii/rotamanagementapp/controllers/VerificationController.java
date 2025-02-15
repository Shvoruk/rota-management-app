package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/verify")
public class VerificationController {

    private final VerificationService verificationService;

    @GetMapping("/{verificationToken}")
    public ResponseEntity<AuthenticationResponse> verify(@PathVariable String verificationToken) {
        return ResponseEntity.ok(verificationService.verify(verificationToken));
    }

    @GetMapping("/resend/{email}")
    public ResponseEntity<String> resendVerificationToken(@PathVariable String email) {
        verificationService.resendVerificationToken(email);
        return ResponseEntity.ok("Please check your email");
    }

}