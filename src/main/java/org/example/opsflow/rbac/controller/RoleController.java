package org.example.opsflow.rbac.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.operationlog.annotation.OperationLog;
import org.example.opsflow.rbac.dto.AssignPermissionsRequest;
import org.example.opsflow.rbac.dto.CreateRoleRequest;
import org.example.opsflow.rbac.dto.RoleDetailResponse;
import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.dto.UpdateRoleRequest;
import org.example.opsflow.rbac.dto.UpdateRoleStatusRequest;
import org.example.opsflow.rbac.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('role:manage')")
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @OperationLog(
            module = "ROLE",
            action = "CREATE"
    )
    @PostMapping
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request){
        return ApiResponse.success(roleService.createRole(request));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getRoleList(){
        return ApiResponse.success(roleService.getRoleList());
    }

    @OperationLog(
            module = "ROLE",
            action = "UPDATE_PERMISSIONS",
            targetIdArg = 0
    )
    @PutMapping("/{id}/permissions")
    public ApiResponse<Void> assignPermissions(
            @PathVariable Long id,
            @Valid @RequestBody AssignPermissionsRequest request
            ){
        roleService.assignPermissions(id,request);
        return ApiResponse.success();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDetailResponse> getRoleDetail(@PathVariable Long id){
        return ApiResponse.success(roleService.getRoleDetail(id));
    }

    @OperationLog(
            module = "ROLE",
            action = "UPDATE",
            targetIdArg = 0
    )
    @PutMapping("/{id}")
    public ApiResponse<RoleResponse> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleRequest request
    ) {
        return ApiResponse.success(roleService.updateRole(id, request));
    }

    @OperationLog(
            module = "ROLE",
            action = "UPDATE_STATUS",
            targetIdArg = 0
    )
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateRoleStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleStatusRequest request
    ) {
        roleService.updateRoleStatus(id, request);
        return ApiResponse.success();
    }
}
