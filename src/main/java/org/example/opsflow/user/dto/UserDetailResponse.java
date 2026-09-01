package org.example.opsflow.user.dto;

import lombok.Data;
import org.example.opsflow.rbac.dto.RoleResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDetailResponse {
    private Long id;

    private String username;

    private String realName;

    private String email;

    private String phone;

    private Long departmentId;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<RoleResponse> roles;
}
