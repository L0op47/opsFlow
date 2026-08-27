package org.example.opsflow.department.service;

import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.dto.UpdateDepartmentRequest;
import org.example.opsflow.department.dto.DepartmentResponse;
import org.example.opsflow.user.dto.UserResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(CreateDepartmentRequest createDepartmentRequest);

    List<DepartmentResponse> getDepartmentList();

    DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request);

    PageResponse<UserResponse> getDepartmentMembers(Long id, int page, int size);
}
