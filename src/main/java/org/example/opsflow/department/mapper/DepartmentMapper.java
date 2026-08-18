package org.example.opsflow.department.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
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

    @Select("SELECT id,name,code,status FROM sys_department ORDER BY id DESC ")
    List<Department> findAll();
}
