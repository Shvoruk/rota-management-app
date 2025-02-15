package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.UserDto;

public interface AuthenticationService {

    void register(UserDto request);
    AuthenticationResponse authenticate(AuthenticationRequest request);

}
