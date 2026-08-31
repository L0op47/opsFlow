package org.example.opsflow.rbac.service;

import org.example.opsflow.rbac.dto.PermissionsResponse;

import java.util.List;

public interface RolePermissionService {
    List<PermissionsResponse> getCurrentRolePermissions(String name);
}
