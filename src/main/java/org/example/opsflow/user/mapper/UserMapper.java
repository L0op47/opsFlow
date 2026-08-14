package org.example.opsflow.user.mapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.opsflow.user.entity.User;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO sys_user(username,password,real_name,email,phone,status)" +
            " VALUES (#{username},#{password},#{realName},#{email},#{phone},#{status})")
    void insert(User user);


    @Select("SELECT COUNT(*)>0 FROM sys_user WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT id,username,password,status,real_name,email,phone,department_id FROM sys_user WHERE username = #{username}")
    User findByUsername(String username);
}
