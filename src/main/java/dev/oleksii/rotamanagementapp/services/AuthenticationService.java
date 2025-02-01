package dev.oleksii.rotamanagementapp.services;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.RegisterRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.SuccessResponse;

public interface AuthenticationService {

    SuccessResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);

}
