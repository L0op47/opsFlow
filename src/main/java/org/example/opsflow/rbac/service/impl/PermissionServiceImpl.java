package org.example.opsflow.rbac.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.rbac.converter.PermissionConverter;
import org.example.opsflow.rbac.dto.PermissionResponse;
import org.example.opsflow.rbac.entity.Permission;
import org.example.opsflow.rbac.mapper.PermissionMapper;
import org.example.opsflow.rbac.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;
    private final PermissionConverter permissionConverter;

    @Override
    public List<PermissionResponse> getPermissionList() {
        List<Permission> permissions = permissionMapper.findAllEnabled();
        return permissionConverter.toListPermissionResponses(permissions);
    }
}
