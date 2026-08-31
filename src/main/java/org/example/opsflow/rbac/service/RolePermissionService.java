package org.example.opsflow.rbac.service;

import org.example.opsflow.rbac.dto.PermissionResponse;

import java.util.List;

public interface RolePermissionService {
    List<PermissionResponse> getCurrentUserPermissions(String name);
}
