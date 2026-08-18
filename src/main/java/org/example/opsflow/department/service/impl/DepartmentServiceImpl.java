package org.example.opsflow.department.service.impl;

import lombok.AllArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.mapper.DepartmentMapper;
import org.example.opsflow.department.response.DepartmentResponse;
import org.example.opsflow.department.service.DepartmentService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;

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

    private  DepartmentResponse toDepartmentResponse(Department department){
        DepartmentResponse response = new DepartmentResponse();
        response.setCode(department.getCode());
        response.setName(department.getName());
        response.setStatus(department.getStatus());
        response.setId(department.getId());

        return response;
    }
}
