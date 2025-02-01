package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.RegisterRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.SuccessResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.UserUpdateRequest;
import dev.oleksii.rotamanagementapp.domain.entities.User;


public interface UserService {

    User createUser(RegisterRequest request);
    SuccessResponse updateUserDetailsByEmail(String email, UserUpdateRequest request);
    SuccessResponse deleteUserByEmail(String email);
}
