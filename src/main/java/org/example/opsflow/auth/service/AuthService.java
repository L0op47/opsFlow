package org.example.opsflow.auth.service;

import jakarta.validation.Valid;
import org.example.opsflow.auth.dto.CurrentUserResponse;
import org.example.opsflow.auth.dto.LoginRequest;
import org.example.opsflow.auth.dto.LoginResponse;
import org.example.opsflow.auth.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest registerRequest);

    LoginResponse login(@Valid LoginRequest loginRequest);

    CurrentUserResponse getCurrentUser(String username);
}
