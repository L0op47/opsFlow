package org.example.opsflow.user.service;


import jakarta.validation.Valid;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.user.dto.AssignDepartmentRequest;
import org.example.opsflow.user.dto.UserResponse;


public interface UserService {
    PageResponse<UserResponse> getUserPage(int page, int size);

    void updateUserStatus(Long id, Integer status);

    void assignDepartment(Long id, @Valid AssignDepartmentRequest request);
}
