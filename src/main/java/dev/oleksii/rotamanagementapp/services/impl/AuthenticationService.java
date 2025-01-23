package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.RegisterRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.RegisterResponse;
import dev.oleksii.rotamanagementapp.exceptions.EmailAlreadyInUseException;
import dev.oleksii.rotamanagementapp.security.JwtService;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public RegisterResponse register(RegisterRequest request) {

        if (repository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use.");
        }

        var token = UUID.randomUUID().toString();

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .verificationToken(token)
                .build();

        repository.save(user);

        String link = "http://localhost:8080/api/v1/auth/verify?token=" + user.getVerificationToken();
        emailService.sendEmail(user.getEmail(), "Verify your email", link);

        return RegisterResponse.builder()
                .response("Please verify your email to finish your registration")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse verify(String token) {

        User user = repository.findByVerificationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        if (user.getVerificationTokenExpirationDate() != null && user.getVerificationTokenExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired verification token");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpirationDate(null);

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
