package org.example.opsflow.department.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DepartmentResponse {
    private Long id;

    private String name;

    private String code;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
