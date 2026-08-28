package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


@Mapper
public interface PermissionMapper {

    int countEnabledByIds(@Param("permissionIds") Set<Long> permissionIds);
}




