package org.example.opsflow.rbac.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.rbac.dto.AssignRolesRequest;
import org.example.opsflow.rbac.mapper.RoleMapper;
import org.example.opsflow.rbac.mapper.UserRoleMapper;
import org.example.opsflow.rbac.service.UserRoleService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public void assignRolesToUser(Long userId, AssignRolesRequest request) {
        User user = userMapper.findById(userId);
        if(user == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        Set<Long> roleIds = request.getRoleIds();
        int validRoleCont = roleMapper.countEnabledByIds(roleIds);
        if(validRoleCont != roleIds.size()){
            throw new BusinessException(
                    ErrorCode.ROLE_NOT_FOUND,
                    "存在不存在或已禁用的角色"
            );
        }
        userRoleMapper.deleteByRoleId(userId);
        int affectedRows = userRoleMapper.batchInsert(userId,roleIds);
        if (affectedRows != roleIds.size()) {
            throw new BusinessException(
                    ErrorCode.DATABASE_OPERATION_FAILED,
                    "用户角色配置失败"
            );
        }
    }
}
