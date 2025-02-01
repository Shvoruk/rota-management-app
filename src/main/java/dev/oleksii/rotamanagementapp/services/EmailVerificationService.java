package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;

public interface EmailVerificationService {

    AuthenticationResponse verify(String token);
    void resendVerification(String email);
    void sendVerificationEmail(User user);
    String generateVerificationToken();
}
