package org.example.opsflow.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.user.dto.AssignDepartmentRequest;
import org.example.opsflow.user.dto.UpdateUserStatusRequest;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping()
    public ApiResponse<PageResponse<UserResponse>> getUserPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ApiResponse.success(userService.getUserPage(page,size));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserStatusRequest updateUserStatusRequest
    ){
        userService.updateUserStatus(id,updateUserStatusRequest.getStatus());
        return ApiResponse.success();
    }

    @PatchMapping("/{id}/department")
    public ApiResponse<Void> assignDepartment(
            @PathVariable Long id,
            @Valid @RequestBody AssignDepartmentRequest request
    ){
        userService.assignDepartment(id,request);
        return ApiResponse.success();
    }
}
