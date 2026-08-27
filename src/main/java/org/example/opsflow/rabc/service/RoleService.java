package org.example.opsflow.rabc.service;

import jakarta.validation.Valid;
import org.example.opsflow.rabc.dto.CreateRoleRequest;
import org.example.opsflow.rabc.dto.RoleResponse;

public interface RoleService {
    RoleResponse createRole(@Valid CreateRoleRequest request);
}
