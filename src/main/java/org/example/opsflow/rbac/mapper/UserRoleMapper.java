package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;


@Mapper
public interface UserRoleMapper {

    int batchInsert(Long userId, Set<Long> roleIds);

    void deleteByUserId(Long userId);
}




