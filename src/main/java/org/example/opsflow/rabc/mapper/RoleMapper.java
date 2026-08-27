package org.example.opsflow.rabc.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.opsflow.rabc.entity.Role;


@Mapper
public interface RoleMapper {

    int insert(Role role);

    Role findById(Long id);
}




