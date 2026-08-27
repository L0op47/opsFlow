package org.example.opsflow.rbac.converter;

import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RoleConverter {
    RoleResponse toRoleResponse(Role role);
}
