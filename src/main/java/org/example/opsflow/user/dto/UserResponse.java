package org.example.opsflow.user.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;

    private String username;

    private String realName;

    private String email;

    private String phone;

    private Long departmentId;

    private String departmentName;

    private Integer status;
}
