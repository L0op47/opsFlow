package org.example.opsflow.rbac.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.rbac.dto.AssignPermissionsRequest;
import org.example.opsflow.rbac.dto.CreateRoleRequest;
import org.example.opsflow.rbac.dto.RoleDetailResponse;
import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request){
        return ApiResponse.success(roleService.createRole(request));
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> getRoleList(){
        return ApiResponse.success(roleService.getRoleList());
    }

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
}
