package org.example.opsflow.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;


@Mapper
public interface UserRoleMapper {

    int batchInsert(
            @Param("userId") Long userId,
            @Param("roleIds") Set<Long> roleIds
    );

    void deleteByUserId(Long userId);
}




