package org.example.opsflow.rabc.converter;

import org.example.opsflow.rabc.dto.RoleResponse;
import org.example.opsflow.rabc.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RoleConverter {
    RoleResponse toRoleResponse(Role role);
}
