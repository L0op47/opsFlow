package org.example.opsflow.common.utils;

import org.example.opsflow.department.entiy.Department;
import org.example.opsflow.department.response.DepartmentResponse;
import org.example.opsflow.user.dto.UserResponse;
import org.example.opsflow.user.entity.User;

public final class  Utils {
    public static DepartmentResponse toDepartmentResponse(Department department){
        DepartmentResponse response = new DepartmentResponse();
        response.setCode(department.getCode());
        response.setName(department.getName());
        response.setStatus(department.getStatus());
        response.setId(department.getId());

        return response;
    }
    public static UserResponse toUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRealName(),
                user.getEmail(),
                user.getPhone(),
                user.getDepartmentId(),
                user.getStatus()
        );
    }
}
