package org.example.opsflow.department.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.department.converter.DepartmentConverter;
import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.dto.UpdateDepartmentRequest;
import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.mapper.DepartmentMapper;
import org.example.opsflow.department.dto.DepartmentResponse;
import org.example.opsflow.department.service.DepartmentService;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;
    private final UserMapper userMapper;
    private final DepartmentConverter departmentConverter;

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest createDepartmentRequest) {
        String name =  createDepartmentRequest.getName().trim();
        String code =  createDepartmentRequest.getCode().trim().toUpperCase(Locale.ROOT);
        Department department = new Department();
        department.setCode(code);
        department.setName(name);
        department.setStatus(1);
        int affectedRows;
        try{
            affectedRows = departmentMapper.insert(department);
            if(affectedRows != 1){
                throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"部门创建失败");
            }
        }catch (DuplicateKeyException e){
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_ALREADY_EXISTS);
        }
        Department savedDepartment = departmentMapper.findById(department.getId());
        return departmentConverter.toDepartmentResponse(savedDepartment);
    }

    @Override
    public List<DepartmentResponse> getDepartmentList() {
        List<Department> departments = departmentMapper.findAll();
        return departmentConverter.toDepartmentResponseList(departments);
    }

    @Override
    public DepartmentResponse getDepartmentDetail(Long id) {
        Department department = exitDepartment(id);
        return departmentConverter.toDepartmentResponse(department);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {

        Department department = exitDepartment(id);
        String name =  request.getName().trim();
        String code =  request.getCode().trim().toUpperCase(Locale.ROOT);
        if(Objects.equals(department.getName(),name) &&  Objects.equals(department.getCode(),code)){
            return departmentConverter.toDepartmentResponse(department);
        }
        department.setName(name);
        department.setCode(code);
        try{
            int affectedRows = departmentMapper.updateDepartment(department);
            if(affectedRows != 1){
                throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"部门修改失败");
            }

        }catch (DuplicateKeyException e){
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_ALREADY_EXISTS);
        }
        Department savedDepartment = departmentMapper.findById(department.getId());
        return departmentConverter.toDepartmentResponse(savedDepartment);
    }

    @Override
    public void updateDepartmentStatus(Long id, Integer status) {
        Department department = exitDepartment(id);
        if(Objects.equals(department.getStatus(), status)){
            return;
        }
        int affectedRows = departmentMapper.updateDepartmentStatus(id, status);
        if(affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"部门状态更新失败");
        }
    }

    @Override
    public PageResponse<UserResponse> getDepartmentMembers(Long id, int page, int size) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        exitDepartment(id);
        PageHelper.startPage(page,size);

        List<UserResponse> records = userMapper.findResponsesByDepartmentId(id);
        PageInfo<UserResponse> pageInfo = new PageInfo<>(records);
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
            throw new BusinessException(ErrorCode.DEPARTMENT_NOT_FOUND);
        }
        return department;
    }


}
