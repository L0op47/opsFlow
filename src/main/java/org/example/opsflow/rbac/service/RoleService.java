package org.example.opsflow.rbac.service;

import jakarta.validation.Valid;
import org.example.opsflow.rbac.dto.CreateRoleRequest;
import org.example.opsflow.rbac.dto.RoleResponse;

public interface RoleService {
    RoleResponse createRole(@Valid CreateRoleRequest request);
}
