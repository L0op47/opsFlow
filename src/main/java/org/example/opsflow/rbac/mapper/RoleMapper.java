package org.example.opsflow.rbac.mapper;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.apache.ibatis.annotations.Mapper;
import org.example.opsflow.rbac.entity.Role;

import java.util.List;
import java.util.Set;


@Mapper
public interface RoleMapper {

    int insert(Role role);

    Role findById(Long id);

    List<Role> findAll();

    int countEnabledByIds(@NotEmpty(message = "角色ID列表不能为空") Set<Long> roleIds);
}




