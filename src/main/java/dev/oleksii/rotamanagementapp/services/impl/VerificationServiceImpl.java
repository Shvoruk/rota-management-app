package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.configuration.VerificationConfig;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.TokenExpiredException;
import dev.oleksii.rotamanagementapp.exceptions.UserAlreadyVerifiedException;
import dev.oleksii.rotamanagementapp.exceptions.UserNotFoundException;
import dev.oleksii.rotamanagementapp.security.JwtService;
import dev.oleksii.rotamanagementapp.services.EmailService;
import dev.oleksii.rotamanagementapp.services.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements EmailVerificationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationConfig verificationConfig;
    private final EmailService emailService;

    @Override
    @Transactional
    public AuthenticationResponse verify(String token) {

        var user = userRepository.findByVerificationToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        if (user.getVerificationTokenExpirationDate() != null && user.getVerificationTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Expired verification token");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpirationDate(null);

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    @Transactional
    public void resendVerification(String email) {

        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if(user.isVerified()) throw new UserAlreadyVerifiedException("User with email " + user.getEmail() + " is already verified.");

        String newToken = generateVerificationToken();
        user.setVerificationToken(newToken);
        user.setVerificationTokenExpirationDate(LocalDateTime.now().plusMinutes(verificationConfig.getTokenExpirationMinutes()));

        userRepository.save(user);

        sendVerificationEmail(user);
    }

    public void sendVerificationEmail(User user) {
        String link = verificationConfig.getVerificationLink() + user.getVerificationToken();
        emailService.sendEmail(user.getEmail(), "Verify your email", link);
    }

    @Override
    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
