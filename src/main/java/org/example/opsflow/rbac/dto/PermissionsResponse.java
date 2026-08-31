package org.example.opsflow.rbac.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermissionsResponse {
    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
