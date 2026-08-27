package org.example.opsflow.rabc.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleResponse {
    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
