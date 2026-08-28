package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;


@Mapper
public interface RolePermissionMapper {

    void deleteByUserId(Long roleId);

    int batchInsert(Long roleId, Set<Long> permissionIds);
}




