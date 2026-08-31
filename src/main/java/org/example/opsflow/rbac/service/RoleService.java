package org.example.opsflow.rbac.service;

import jakarta.validation.Valid;
import org.example.opsflow.rbac.dto.AssignPermissionsRequest;
import org.example.opsflow.rbac.dto.CreateRoleRequest;
import org.example.opsflow.rbac.dto.RoleDetailResponse;
import org.example.opsflow.rbac.dto.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(@Valid CreateRoleRequest request);

    List<RoleResponse> getRoleList();

    void assignPermissions(Long roleId, @Valid AssignPermissionsRequest request);

    RoleDetailResponse getRoleDetail(Long id);
}
