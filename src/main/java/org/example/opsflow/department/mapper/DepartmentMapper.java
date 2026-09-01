package org.example.opsflow.department.mapper;

import org.apache.ibatis.annotations.*;
import org.example.opsflow.department.entiy.Department;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Insert("INSERT INTO sys_department(name,code,status)" +
            "VALUES (#{name},#{code},#{status})")
    @Options(
            useGeneratedKeys = true,
            keyColumn = "id",
            keyProperty = "id"
    )
    int inset(Department department);

    @Select("SELECT id,name,code,status,created_at,updated_at FROM sys_department ORDER BY id DESC ")
    List<Department> findAll();

    @Update("UPDATE sys_department SET name = #{name},code = #{code} WHERE id = #{id}")
    int updateDepartment(Department department);

    @Select("SELECT id,name,code,status,created_at,updated_at FROM sys_department WHERE id = #{id}")
    Department findById(Long id);

    @Update("UPDATE sys_department SET status = #{status} WHERE id = #{id}")
    int updateDepartmentStatus(@Param("id") Long id, @Param("status") Integer status);
}
