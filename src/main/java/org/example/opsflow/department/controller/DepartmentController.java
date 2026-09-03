package org.example.opsflow.department.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.department.dto.CreateDepartmentRequest;
import org.example.opsflow.department.dto.DepartmentResponse;
import org.example.opsflow.department.dto.UpdateDepartmentRequest;
import org.example.opsflow.department.dto.UpdateDepartmentStatusRequest;
import org.example.opsflow.department.service.DepartmentService;
import org.example.opsflow.operationlog.annotation.OperationLog;
import org.example.opsflow.user.dto.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    @OperationLog(
            module = "DEPARTMENT",
            action = "CREATE"
    )
    @PreAuthorize("hasAuthority('department:manage')")
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

    @PreAuthorize("hasAuthority('department:manage')")
    @GetMapping("/{id}")
    public ApiResponse<DepartmentResponse> getDepartmentDetail(@PathVariable Long id){
        return ApiResponse.success(departmentService.getDepartmentDetail(id));
    }

    @OperationLog(
            module = "DEPARTMENT",
            action = "UPDATE",
            targetIdArg = 0
    )
    @PreAuthorize("hasAuthority('department:manage')")
    @PutMapping("/{id}")
    public ApiResponse<DepartmentResponse> updateDepartment(@PathVariable Long id,
                                                            @Valid @RequestBody UpdateDepartmentRequest request){
        DepartmentResponse departmentResponse = departmentService.updateDepartment(id,request);
        return ApiResponse.success(departmentResponse);

    }

    @OperationLog(
            module = "DEPARTMENT",
            action = "UPDATE_STATUS",
            targetIdArg = 0
    )
    @PreAuthorize("hasAuthority('department:manage')")
    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateDepartmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDepartmentStatusRequest request
    ){
        departmentService.updateDepartmentStatus(id, request.getStatus());
        return ApiResponse.success();
    }

    @PreAuthorize("hasAuthority('department:manage')")
    @GetMapping("/{id}/members")
    public ApiResponse<PageResponse<UserResponse>> getDepartmentMembers(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1",name = "page") int page,
            @RequestParam(defaultValue = "10",name = "size") int size
    ){
        return ApiResponse.success(departmentService.getDepartmentMembers(id,page,size));
    }
}
