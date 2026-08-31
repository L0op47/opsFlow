package org.example.opsflow.rbac.converter;

import org.example.opsflow.rbac.dto.PermissionResponse;
import org.example.opsflow.rbac.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PermissionConverter {
    List<PermissionResponse> toListPermissionResponses(List<Permission> permissions);

    PermissionResponse toPermissionResponse(Permission permission);
}
