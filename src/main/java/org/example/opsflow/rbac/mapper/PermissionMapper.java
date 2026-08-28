package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;


@Mapper
public interface PermissionMapper {

    int countEnabledByIds(Set<Long> permissionIds);
}




