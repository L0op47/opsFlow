package org.example.opsflow.rbac.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.rbac.dto.PermissionResponse;
import org.example.opsflow.rbac.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('role:manage')")
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(name = "权限管理", description = "系统权限信息查询")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getPermissionList() {
        return ApiResponse.success(permissionService.getPermissionList());
    }
}
