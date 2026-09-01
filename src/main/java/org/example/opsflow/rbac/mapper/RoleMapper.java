package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.opsflow.rbac.entity.Role;

import java.util.List;
import java.util.Set;


@Mapper
public interface RoleMapper {

    int insert(Role role);

    Role findById(Long id);

    List<Role> findAll();

    int countEnabledByIds(@Param("roleIds") Set<Long> roleIds);

    int updateBasicInfo(Role role);

    int updateStatus(
            @Param("id") Long id,
            @Param("status") Integer status
    );
}



