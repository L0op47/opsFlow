package org.example.opsflow.department.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.dto.UpdateDepartmentRequest;
import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.mapper.DepartmentMapper;
import org.example.opsflow.department.response.DepartmentResponse;
import org.example.opsflow.department.service.DepartmentService;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.example.opsflow.common.utils.Utils.toDepartmentResponse;
import static org.example.opsflow.common.utils.Utils.toUserResponse;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;
    private final UserMapper userMapper;

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest createDepartmentRequest) {
        String name =  createDepartmentRequest.getName().trim();
        String code =  createDepartmentRequest.getCode().trim().toUpperCase();
        Department department = new Department();
        department.setCode(code);
        department.setName(name);
        department.setStatus(1);
        int affectedRows;
        try{
            affectedRows = departmentMapper.inset(department);
            if(affectedRows != 1){
                throw new BusinessException(50001,"部门创建失败");
            }
        }catch (DuplicateKeyException e){
            throw new BusinessException(40007,"部门代码已经存在");
        }
        return toDepartmentResponse(department);
    }

    @Override
    public List<DepartmentResponse> getDepartmentList() {
        List<Department> departments = departmentMapper.findAll();

        List<DepartmentResponse> departmentResponses = new ArrayList<>();
        for(Department department : departments){
            departmentResponses.add(toDepartmentResponse(department));
        }
        return departmentResponses;
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {

        Department department = exitDepartment(id);
        String name =  request.getName().trim();
        String code =  request.getCode().trim().toUpperCase(Locale.ROOT);
        if(Objects.equals(department.getName(),name) &&  Objects.equals(department.getCode(),code)){
            return toDepartmentResponse(department);
        }
        department.setName(name);
        department.setCode(code);
        try{
            int affectedRows = departmentMapper.updateDepartment(department);
            if(affectedRows != 1){
                throw new BusinessException(50002,"部门修改失败");
            }

        }catch (DuplicateKeyException e){
            throw new BusinessException(40007,"部门代码已经存在");
        }

        return toDepartmentResponse(department);
    }

    @Override
    public PageResponse<UserResponse> getDepartmentMembers(Long id, int page, int size) {
        if(page < 1){
            throw new BusinessException(40004,"页码必须大于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(40004,"每页的数量必须在1-100之间");
        }
        exitDepartment(id);
        PageHelper.startPage(page,size);

        List<User> users = userMapper.findByDepartmentId(id);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        List<UserResponse> records = new ArrayList<>();
        for(User user : users){
            records.add(toUserResponse(user));
        }
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );

    }

    private Department exitDepartment(Long id){
        Department department = departmentMapper.findById(id);
        if(department == null){
            throw new BusinessException(40008,"该部门不存在");
        }
        return department;
    }


}
