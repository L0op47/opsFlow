package org.example.opsflow.department.service;

import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(CreateDepartmentRequest createDepartmentRequest);

    List<DepartmentResponse> getDepartmentList();
}
