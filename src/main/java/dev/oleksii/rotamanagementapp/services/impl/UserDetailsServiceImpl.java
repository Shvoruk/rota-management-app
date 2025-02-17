package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.UserDto;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import dev.oleksii.rotamanagementapp.domain.enums.Role;
import dev.oleksii.rotamanagementapp.domain.repos.UserRepository;
import dev.oleksii.rotamanagementapp.exceptions.ConflictException;
import dev.oleksii.rotamanagementapp.services.UserService;
import dev.oleksii.rotamanagementapp.services.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of user-related services, including user creation,
 * updating, deletion, and details retrieval for security features.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    /**
     * Deletes the provided user from the system.
     *
     * @param user The user entity to be deleted.
     */
    @Override
    @Transactional
    public void deleteUser(User user) {
        // Removes the user from the user repository
        userRepository.delete(user);
    }

    /**
     * Updates user details (name, email, password) based on the provided UserDto,
     * then saves changes. If the email changes, un-verifies the user and sends a new verification token.
     *
     * @param user    The existing user entity to update.
     * @param request The DTO containing new details for the user.
     */
    @Override
    @Transactional
    public void updateUserDetails(User user, UserDto request) {

        // Update user's full name if provided in request
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        // Update user's email if provided in request
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {

            // Check if the new email already exists in the system
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email is already in use");
            }

            // Update the user's email and mark them as unverified
            user.setEmail(request.getEmail());
            user.setVerified(false);

            // Send a new verification token for the updated email
            verificationService.resendVerificationToken(request.getEmail());
        }

        // Update user's password if provided in request
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {

            // Prevent reuse of the same password
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("New password is same as previous password");
            }

            // Encode and save the new password
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Persist the updated user entity to the database
        userRepository.save(user);
    }

    /**
     * Creates a new user entity in the database based on a UserDto object,
     * then generates and assigns a verification token to the newly created user.
     *
     * @param request The DTO containing user registration details.
     * @return The newly created User entity.
     */
    @Override
    @Transactional
    public User createUser(UserDto request) {
        // Build the new User object, setting default fields
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Save the user in the repository
        userRepository.save(user);

        // Create and assign a verification token for the new user
        verificationService.createVerificationToken(user);

        // Return the newly created user object
        return user;
    }

    /**
     * Loads user details for security authentication using Spring Security's UserDetails interface.
     *
     * @param username The email/username used to look up the user.
     * @return The UserDetails of the found user.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Attempt to find user by email, or throw if not found
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
