package org.example.opsflow.rbac.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.rbac.converter.PermissionConverter;
import org.example.opsflow.rbac.converter.RoleConverter;
import org.example.opsflow.rbac.dto.*;
import org.example.opsflow.rbac.entity.Permission;
import org.example.opsflow.rbac.entity.Role;
import org.example.opsflow.rbac.mapper.PermissionMapper;
import org.example.opsflow.rbac.mapper.RoleMapper;
import org.example.opsflow.rbac.mapper.RolePermissionMapper;
import org.example.opsflow.rbac.service.RoleService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleConverter roleConverter;
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionConverter permissionConverter;


    @Override
    public RoleResponse createRole(CreateRoleRequest request) {
        String code =  request.getCode().trim().toUpperCase(Locale.ROOT);
        String name =  request.getName().trim();
        Role role = new Role();
        role.setName(name);
        role.setDescription(request.getDescription());
        role.setCode(code);
        role.setStatus(1);
        int affectedRows;
        try{
            affectedRows = roleMapper.insert(role);
            if(affectedRows != 1){
                throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"角色创建失败");
            }
        }catch (DuplicateKeyException e){
            throw new BusinessException(ErrorCode.ROLE_CODE_ALREADY_EXISTS);
        }
        Role savedRole = roleMapper.findById(role.getId());
        return roleConverter.toRoleResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getRoleList() {
        List<Role> roles = roleMapper.findAll();
        return roleConverter.toListRoleResponse(roles);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, AssignPermissionsRequest request) {
        Role role = roleMapper.findById(roleId);
        if(role == null){
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }
        Set<Long> permissionIds = request.getPermissionIds();
        int validPermissionCount = permissionMapper.countEnabledByIds(permissionIds);
        if(validPermissionCount != permissionIds.size()){
            throw new BusinessException(
                    ErrorCode.PERMISSION_NOT_FOUND,
                    "存在无效或已禁用的权限"
            );
        }
        rolePermissionMapper.deleteByRoleId(roleId);
        int affectedRows = rolePermissionMapper.batchInsert(roleId,permissionIds);
        if (affectedRows != permissionIds.size()) {
            throw new BusinessException(
                    ErrorCode.DATABASE_OPERATION_FAILED,
                    "角色权限配置失败"
            );
        }
    }

    @Override
    public RoleDetailResponse getRoleDetail(Long id) {
        Role role =  roleMapper.findById(id);
        if(role == null){
            throw new BusinessException(ErrorCode.ROLE_NOT_FOUND);
        }
        if (!Objects.equals(role.getStatus(),1)){
            throw new BusinessException(ErrorCode.ROLE_DISABLED);
        }
        List<Permission> permissions = permissionMapper.findEnabledByRoleId(id);
        RoleDetailResponse response = roleConverter.toRoleDetailResponse(role);
        response.setPermissions(permissionConverter.toListPermissionResponses(permissions));
        return response;
    }
}
