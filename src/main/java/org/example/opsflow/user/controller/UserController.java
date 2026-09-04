package org.example.opsflow.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.operationlog.annotation.OperationLog;
import org.example.opsflow.rbac.dto.AssignRolesRequest;
import org.example.opsflow.rbac.service.UserRoleService;
import org.example.opsflow.user.dto.AssignDepartmentRequest;
import org.example.opsflow.user.dto.UpdateUserRequest;
import org.example.opsflow.user.dto.UpdateUserStatusRequest;
import org.example.opsflow.user.dto.UserDetailResponse;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAuthority('user:manage')")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息和用户状态管理")
public class UserController {
    private final UserService userService;
    private final UserRoleService userRoleService;

    @GetMapping("/{id}")
    public ApiResponse<UserDetailResponse> getUserDetail(@PathVariable Long id) {
        UserDetailResponse response = userService.getUserDetail(id);
        response.setRoles(userRoleService.getAssignedRolesByUserId(id));
        return ApiResponse.success(response);
    }

    @OperationLog(
            module = "USER",
            action = "UPDATE",
            targetIdArg = 0
    )
    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateBasicInfo(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return ApiResponse.success(userService.updateBasicInfo(id, request));
    }

    @GetMapping
    public ApiResponse<PageResponse<UserResponse>> getUserPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ApiResponse.success(userService.getUserPage(page,size));
    }

    @OperationLog(
            module = "USER",
            action = "UPDATE_STATUS",
            targetIdArg = 0
    )
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest updateUserStatusRequest
    ){
        userService.updateUserStatus(id,updateUserStatusRequest.getStatus());
        return ApiResponse.success();
    }

    @OperationLog(
            module = "USER",
            action = "UPDATE_DEPARTMENT",
            targetIdArg = 0
    )
    @PatchMapping("/{id}/department")
    public ApiResponse<Void> assignDepartment(
            @PathVariable Long id,
            @Valid @RequestBody AssignDepartmentRequest request
    ){
        userService.assignDepartment(id,request);
        return ApiResponse.success();
    }

    @OperationLog(
            module = "USER",
            action = "UPDATE_ROLES",
            targetIdArg = 0
    )
    @PutMapping("/{id}/roles")
    public ApiResponse<Void> assignRolesToUser(
            @PathVariable Long id,
            @Valid @RequestBody AssignRolesRequest request
    ){
        userRoleService.assignRolesToUser(id,request);
        return ApiResponse.success();
    }
}
