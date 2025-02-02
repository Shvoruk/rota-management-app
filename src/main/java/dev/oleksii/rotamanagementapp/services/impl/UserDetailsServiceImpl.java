package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.configuration.EmailVerificationConfig;
import dev.oleksii.rotamanagementapp.domain.dtos.RegisterRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.UserUpdateRequest;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.EmailAlreadyInUseException;
import dev.oleksii.rotamanagementapp.exceptions.UserNotFoundException;
import dev.oleksii.rotamanagementapp.services.UserService;
import dev.oleksii.rotamanagementapp.services.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationConfig emailVerificationConfig;
    private final EmailVerificationService emailVerificationService;


    @Transactional
    @Override
    public void deleteUserByEmail(String email) {

        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));

        userRepository.delete(user);

    }

    @Transactional
    @Override
    public void updateUserDetailsByEmail(String email, UserUpdateRequest request) {

        var user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getNewFullName() != null && !request.getNewFullName().isEmpty()) {
            user.setFullName(request.getNewFullName());
        }

        if (request.getNewEmail() != null && !request.getNewEmail().isEmpty()) {

            if (userRepository.existsByEmail(request.getNewEmail()))
                throw new EmailAlreadyInUseException("Email is already in use");

            user.setEmail(request.getNewEmail());
            user.setVerified(false);
            emailVerificationService.resendVerification(request.getNewEmail());
        }

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {

            if (passwordEncoder.matches(request.getNewPassword(), user.getPassword()))
                throw new IllegalArgumentException("New password is same as previous password");
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);

    }

    @Transactional
    @Override
    public User createUser(RegisterRequest request) {

        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .verificationToken(emailVerificationService.generateVerificationToken())
                .verificationTokenExpirationDate(LocalDateTime.now().plusMinutes(emailVerificationConfig.getTokenExpirationMinutes()))
                .creationDate(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
