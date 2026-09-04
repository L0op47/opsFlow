package org.example.opsflow.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.auth.dto.CurrentUserResponse;
import org.example.opsflow.auth.dto.LoginRequest;
import org.example.opsflow.auth.dto.LoginResponse;
import org.example.opsflow.auth.dto.RegisterRequest;
import org.example.opsflow.auth.service.AuthService;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.rbac.dto.PermissionResponse;
import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.service.RolePermissionService;
import org.example.opsflow.rbac.service.UserRoleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "认证管理",
        description = "用户注册、登录、退出和当前用户信息"
)
public class AuthController {
    private final AuthService authService;
    private final UserRoleService userRoleService;
    private final RolePermissionService rolePermissionService;

    @SecurityRequirements
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest){
        authService.register(registerRequest);
        return ApiResponse.success();
    }

    @SecurityRequirements
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ApiResponse.success(authService.login(loginRequest));
    }


    @GetMapping("/me")
    public ApiResponse<CurrentUserResponse> getCurrentUser(Authentication authentication){
        String username = authentication.getName();
        return ApiResponse.success(authService.getCurrentUser(username));
    }

    @GetMapping("/me/roles")
    public ApiResponse<List<RoleResponse>> getCurrentUserRoles(Authentication authentication){
        return ApiResponse.success(userRoleService.getCurrentUserRoles(authentication.getName()));

    }

    @GetMapping("/me/permissions")
    public ApiResponse<List<PermissionResponse>> getCurrentUserPermissions(Authentication authentication){
        return ApiResponse.success(rolePermissionService.getCurrentUserPermissions(authentication.getName()));

    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication authentication){
        authService.logout(authentication.getName());
        return ApiResponse.success();
    }
}
