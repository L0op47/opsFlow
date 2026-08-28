package org.example.opsflow.rbac.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.rbac.dto.AssignPermissionsRequest;
import org.example.opsflow.rbac.dto.CreateRoleRequest;
import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.service.RoleService;
import org.example.opsflow.user.service.UserService;
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
    public ApiResponse<Void> assignPermission(
            @PathVariable Long id,
            @Valid @RequestBody AssignPermissionsRequest request
            ){
        roleService.assignPermission(id,request);
        return ApiResponse.success();
    }

}
