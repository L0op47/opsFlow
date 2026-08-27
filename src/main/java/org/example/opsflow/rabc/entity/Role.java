package org.example.opsflow.rabc.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Role {
    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}