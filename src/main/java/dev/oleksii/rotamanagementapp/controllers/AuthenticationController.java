package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationRequest;
import dev.oleksii.rotamanagementapp.domain.dtos.AuthenticationResponse;
import dev.oleksii.rotamanagementapp.domain.dtos.UserDto;
import dev.oleksii.rotamanagementapp.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto request){
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request){
        return  ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
