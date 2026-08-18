package org.example.opsflow.department.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.response.DepartmentResponse;
import org.example.opsflow.department.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    @PostMapping
    public ApiResponse<DepartmentResponse> createDepartment(
           @Valid @RequestBody CreateDepartmentRequest createDepartmentRequest
    ){
        DepartmentResponse departmentResponse = departmentService.createDepartment(createDepartmentRequest);
        return ApiResponse.success(departmentResponse);
    }

    @GetMapping
    public ApiResponse<List<DepartmentResponse>> getDepartmentList(){
        List<DepartmentResponse> departments = departmentService.getDepartmentList();
        return ApiResponse.success(departments);
    }
}
