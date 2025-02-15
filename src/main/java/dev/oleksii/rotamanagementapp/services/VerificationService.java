package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;

public interface VerificationService {
    void createVerificationToken(User user);
    AuthenticationResponse verify(String token);
    void resendVerificationToken(String email);
    void sendVerificationToken(User user);
    String generateVerificationToken();
}
