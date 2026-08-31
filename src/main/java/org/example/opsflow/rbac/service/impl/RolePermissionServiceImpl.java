package org.example.opsflow.rbac.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.rbac.converter.PermissionConverter;
import org.example.opsflow.rbac.dto.PermissionResponse;
import org.example.opsflow.rbac.entity.Permission;
import org.example.opsflow.rbac.mapper.PermissionMapper;
import org.example.opsflow.rbac.service.RolePermissionService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {
    private final UserService userService;
    private final PermissionMapper permissionMapper;
    private final PermissionConverter permissionConverter;

    @Override
    public List<PermissionResponse> getCurrentUserPermissions(String name) {
        User currentUser = userService.getActiveUser(name);
        List<Permission> permissions = permissionMapper.findEnabledByUserId(currentUser.getId());
        return permissionConverter.toListPermissionResponses(permissions);
    }
}
