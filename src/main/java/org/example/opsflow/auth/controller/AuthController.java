package org.example.opsflow.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.auth.dto.LoginRequest;
import org.example.opsflow.auth.dto.LoginResponse;
import org.example.opsflow.auth.dto.RegisterRequest;
import org.example.opsflow.auth.service.AuthService;
import org.example.opsflow.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest){
        authService.register(registerRequest);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ApiResponse.success(authService.login(loginRequest));
    }

}
