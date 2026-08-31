package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


@Mapper
public interface RolePermissionMapper {

    void deleteByRoleId(@Param("roleId")Long roleId);

    int batchInsert(
            @Param("roleId")Long roleId,
            @Param("permissionIds") Set<Long> permissionIds
    );
}




