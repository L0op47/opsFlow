package org.example.opsflow.rbac.service;

import jakarta.validation.Valid;
import org.example.opsflow.rbac.dto.AssignRolesRequest;
import org.example.opsflow.rbac.dto.RoleResponse;

import java.util.List;

public interface UserRoleService {
    void assignRolesToUser(Long userId, @Valid AssignRolesRequest request);

    List<RoleResponse> getCurrentUserRoles(String name);

    List<RoleResponse> getAssignedRolesByUserId(Long userId);
}
