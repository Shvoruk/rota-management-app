package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.UserDto;
import dev.oleksii.rotamanagementapp.security.SecurityUtil;
import dev.oleksii.rotamanagementapp.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final SecurityUtil securityUtil;

    @PutMapping
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody UserDto request, Principal principal) {
        var user = securityUtil.getCurrentUser(principal);
        userService.updateUserDetails(user, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(Principal principal) {
        var user = securityUtil.getCurrentUser(principal);
        userService.deleteUser(user);
        return ResponseEntity.noContent().build();
    }

}
