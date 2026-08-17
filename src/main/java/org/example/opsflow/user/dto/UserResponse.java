package org.example.opsflow.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    private String realName;

    private String email;

    private String phone;

    private Long departmentId;

    private Integer status;
}
