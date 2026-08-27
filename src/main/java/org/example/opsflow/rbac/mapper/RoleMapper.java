package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.opsflow.rbac.entity.Role;


@Mapper
public interface RoleMapper {

    int insert(Role role);

    Role findById(Long id);
}




