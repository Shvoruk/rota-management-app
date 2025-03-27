package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.UpdateUserRequest;
import dev.oleksii.rotamanagementapp.security.SecurityUtil;
import dev.oleksii.rotamanagementapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * REST controller for managing account-related operations for the currently authenticated user.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    // Utility to retrieve the current authenticated user based on the provided Principal.
    private final SecurityUtil securityUtil;

    /**
     * Updates the account details of the currently authenticated user.
     *
     * @param request   A validated CreateUserRequest containing updated account information.
     * @param principal The security principal representing the current authenticated user.
     * @return ResponseEntity with HTTP status 204 (No Content) indicating a successful update.
     */
    @PutMapping
    public ResponseEntity<String> updateAccount(
            @Valid @RequestBody UpdateUserRequest request,
            Principal principal) {

        // Retrieve the currently authenticated user based on the provided Principal.
        var user = securityUtil.getCurrentUser(principal);
        // Update the user's account details.
        userService.updateUserDetails(user, request);
        return ResponseEntity.ok("User updated successfully.");
    }

    /**
     * Deletes the account of the currently authenticated user.
     *
     * @param principal The security principal representing the current authenticated user.
     * @return ResponseEntity with HTTP status 204 (No Content) indicating successful deletion.
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAccount(Principal principal) {
        // Retrieve the current authenticated user.
        var user = securityUtil.getCurrentUser(principal);
        // Delete user account.
        userService.deleteUser(user);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
