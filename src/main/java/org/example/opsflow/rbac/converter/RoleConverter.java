package org.example.opsflow.rbac.converter;

import org.example.opsflow.rbac.dto.RoleDetailResponse;
import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RoleConverter {
    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toListRoleResponse(List<Role> roles);

    @Mapping(target = "permissions", ignore = true)
    RoleDetailResponse toRoleDetailResponse(Role role);
}
