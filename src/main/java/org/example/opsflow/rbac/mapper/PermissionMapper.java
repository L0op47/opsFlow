package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.opsflow.rbac.entity.Permission;

import java.util.List;
import java.util.Set;


@Mapper
public interface PermissionMapper {

    int countEnabledByIds(@Param("permissionIds") Set<Long> permissionIds);

    List<Permission> findEnabledByUserId(@Param("userId")Long userId);

    List<Permission> findEnabledByRoleId(@Param("roleId")Long roleId);

    List<Permission> findAllEnabled();
}



