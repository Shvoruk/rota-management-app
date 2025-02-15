package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.configuration.VerificationConfig;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.entities.VerificationToken;
import dev.oleksii.rotamanagementapp.domain.repos.TokenRepository;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.TokenExpiredException;
import dev.oleksii.rotamanagementapp.exceptions.UserAlreadyVerifiedException;
import dev.oleksii.rotamanagementapp.exceptions.UserNotFoundException;
import dev.oleksii.rotamanagementapp.security.JwtService;
import dev.oleksii.rotamanagementapp.services.EmailService;
import dev.oleksii.rotamanagementapp.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationConfig verificationConfig;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public AuthenticationResponse verify(String token) {
        var verificationToken = tokenRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));
        if (verificationToken.getExpirationDate() != null && verificationToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Expired verification token");
        }
        var user = verificationToken.getUser();
        user.setVerified(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void createVerificationToken(User user){
            var verificationToken = VerificationToken.builder()
                    .token(generateVerificationToken())
                    .expirationDate(LocalDateTime.now().plusMinutes(verificationConfig.getTokenExpirationMinutes()))
                    .user(user)
                    .build();
        tokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public void resendVerificationToken(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"));

        if(user.isVerified()) throw new UserAlreadyVerifiedException("User with email " + user.getEmail() + " is already verified.");

        var verificationToken = tokenRepository.findByUser(user).orElseThrow();
        verificationToken.setToken(generateVerificationToken());
        verificationToken.setExpirationDate(LocalDateTime.now().plusMinutes(verificationConfig.getTokenExpirationMinutes()));

        tokenRepository.save(verificationToken);

        sendVerificationToken(user);
    }

    public void sendVerificationToken(User user) {
        var verificationToken = tokenRepository.findByUser(user).orElseThrow();
        String link = verificationConfig.getVerificationLink() + verificationToken.getToken();
        emailService.sendEmail(user.getEmail(), "Verify your email", link);
    }

    @Override
    public String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
