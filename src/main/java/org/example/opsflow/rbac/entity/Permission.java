package org.example.opsflow.rbac.entity;

import java.time.LocalDateTime;
import lombok.Data;


@Data
public class Permission {
    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}