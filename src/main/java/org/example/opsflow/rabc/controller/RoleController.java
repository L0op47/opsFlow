package org.example.opsflow.rabc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.rabc.dto.CreateRoleRequest;
import org.example.opsflow.rabc.dto.RoleResponse;
import org.example.opsflow.rabc.service.RoleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping()
    public ApiResponse<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request){
        return ApiResponse.success(roleService.createRole(request));
    }

}
