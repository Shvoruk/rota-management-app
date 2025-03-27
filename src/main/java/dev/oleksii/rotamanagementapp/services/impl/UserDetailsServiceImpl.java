package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.dtos.CreateUserRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.UpdateUserRequest;
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
 * Implementation of {@link UserService}.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    /**
     * Deletes the provided user from the system
     *
     * @param user The user entity to be deleted.
     */
    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    /**
     * Updates the details of the specified user based on the provided CreateUserRequest by:
     * <ul>
     *   <li>Updating the user's full name if a new name is provided.</li>
     *   <li>Checking if a new email is provided:
     *     <ul>
     *       <li>If so, verifying that the email is not already in use.</li>
     *       <li>Updating the user's email, marking them as unverified, and resending a verification token.</li>
     *     </ul>
     *   </li>
     *   <li>Updating the user's password if a new password is provided.</li>
     *   <li>Persisting the updated user entity to the database.</li>
     * </ul>
     *
     * @param user    The existing user entity to update.
     * @param request The DTO containing new details for the user.
     */
    @Override
    @Transactional
    public void updateUserDetails(User user, UpdateUserRequest request) {
        // Update full name if provided.
        if (request.getFullName() != null && !request.getFullName().isEmpty()) {
            user.setFullName(request.getFullName());
        }

        // Update email if provided.
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            // Ensure the new email is not already taken.
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email is already in use. Please choose a different email address.");
            }
            // Update email, mark user as unverified, and resend verification token.
            user.setEmail(request.getEmail());
            user.setVerified(false);
            verificationService.resendVerificationToken(request.getEmail());
        }

        // Update password if provided.
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            // Encode and update the password.
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Save the updated user entity.
        userRepository.save(user);
    }

    /**
     * Creates a new user in the system based on the provided CreateUserRequest by:
     * <ul>
     *   <li>Building a new User entity with default properties (role, verification status, creation timestamp).</li>
     *   <li>Encoding the user's password.</li>
     *   <li>Generating and assigning a verification token to the new user.</li>
     *   <li>Persisting the user entity.</li>
     * </ul>
     *
     * @param request The DTO containing user registration details.
     * @return The newly created User entity.
     */
    @Override
    @Transactional
    public User createUser(CreateUserRequest request) {
        var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verified(false)
                .createdAt(LocalDateTime.now())
                .build();

        verificationService.createVerificationToken(user);
        // Persisting the user is handled by the createVerificationToken method.
        return user;
    }

    /**
     * Loads user details for authentication by:
     * <ul>
     *   <li>Retrieving the user by their email from the user repository.</li>
     *   <li>Throwing a {@link UsernameNotFoundException} if no user is found with the provided email.</li>
     * </ul>
     *
     * @param username The email used to look up the user.
     * @return The UserDetails of the found user.
     * @throws UsernameNotFoundException if the user is not found in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}