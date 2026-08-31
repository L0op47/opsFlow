package org.example.opsflow.rbac.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.rbac.converter.PermissionConverter;
import org.example.opsflow.rbac.dto.PermissionsResponse;
import org.example.opsflow.rbac.entity.Permission;
import org.example.opsflow.rbac.mapper.PermissionMapper;
import org.example.opsflow.rbac.service.RolePermissionService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RolePermissionServiceImpl implements RolePermissionService {
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final PermissionConverter permissionConverter;

    @Override
    public List<PermissionsResponse> getCurrentRolePermissions(String name) {
        User user = userMapper.findByUsername(name);
        if(user == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if(!Objects.equals(user.getStatus(),1)){
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        List<Permission> permissions = permissionMapper.findEnabledByUserId(user.getId());
        return permissionConverter.toListPermissionResponses(permissions);
    }
}
