package org.example.opsflow.rbac.service;

import jakarta.validation.Valid;
import org.example.opsflow.rbac.dto.AssignRolesRequest;

public interface UserRoleService {
    void assignRolesToUser(Long userId, @Valid AssignRolesRequest request);
}
