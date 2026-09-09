package org.example.opsflow.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.opsflow.user.dto.UserDetailResponse;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO sys_user(username,password,real_name,email,phone,status)" +
            " VALUES (#{username},#{password},#{realName},#{email},#{phone},#{status})")
    void insert(User user);


    @Select("SELECT COUNT(*)>0 FROM sys_user WHERE username = #{username}")
    boolean existsByUsername(String username);

    @Select("SELECT id,username,password,status,real_name,email,phone,department_id FROM sys_user WHERE username = #{username}")
    User findByUsername(String username);


    List<User> findAll();

    List<UserResponse> findAllResponses();


    @Update("UPDATE sys_user SET status = #{status},updated_at = LOCALTIMESTAMP WHERE id = #{id}")
    int updateUserStatus(Long id, Integer status);

    @Select("SELECT id,username,password,status,real_name,email,phone,department_id,created_at,updated_at FROM sys_user WHERE id = #{id}")
    User findById(Long id);

    UserResponse findResponseById(Long id);

    UserDetailResponse findDetailResponseById(Long id);

    @Update("UPDATE sys_user SET real_name = #{realName},email = #{email},phone = #{phone},updated_at = LOCALTIMESTAMP WHERE id = #{id}")
    int updateBasicInfo(User user);

    @Update("UPDATE sys_user SET department_id = #{departmentId},updated_at = LOCALTIMESTAMP WHERE id = #{id}")
    int updateUserDepartment(Long id, Long departmentId);


    List<User> findByDepartmentId(Long id);

    List<UserResponse> findResponsesByDepartmentId(Long id);
}
