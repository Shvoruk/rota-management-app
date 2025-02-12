package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.SuccessResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.UserUpdateRequest;
import dev.oleksii.rotamanagementapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateAccount(@Valid @RequestBody UserUpdateRequest request, Principal principal) {
        userService.updateUserDetailsByEmail(principal.getName(), request);
        return ResponseEntity.ok(new SuccessResponse("User updated successfully"));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteAccount(Principal principal) {
        userService.deleteUserByEmail(principal.getName());
        return ResponseEntity.ok(new SuccessResponse("User deleted successfully"));
    }
}
