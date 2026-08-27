package org.example.opsflow.rbac.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.rbac.converter.RoleConverter;
import org.example.opsflow.rbac.dto.CreateRoleRequest;
import org.example.opsflow.rbac.dto.RoleResponse;
import org.example.opsflow.rbac.entity.Role;
import org.example.opsflow.rbac.mapper.RoleMapper;
import org.example.opsflow.rbac.service.RoleService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleConverter roleConverter;


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
}
