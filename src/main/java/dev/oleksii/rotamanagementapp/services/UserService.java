package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.RegisterRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.UserUpdateRequest;
import dev.oleksii.rotamanagementapp.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {

    User createUser(RegisterRequest request);
    void updateUserDetailsByEmail(String email, UserUpdateRequest request);
    void deleteUserByEmail(String email);
}
