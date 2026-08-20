package org.example.opsflow.department.service;

import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.dto.UpdateDepartmentRequest;
import org.example.opsflow.department.response.DepartmentResponse;
import org.example.opsflow.user.dto.UserResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(CreateDepartmentRequest createDepartmentRequest);

    List<DepartmentResponse> getDepartmentList();

    DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request);

    List<UserResponse> getDepartmentMembers(Long id, int page, int size);
}
