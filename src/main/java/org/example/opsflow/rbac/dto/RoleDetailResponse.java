package org.example.opsflow.rbac.dto;

import lombok.Data;
import org.example.opsflow.rbac.entity.Permission;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleDetailResponse {
    private Long id;

    private String code;

    private String name;

    private String description;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Permission> permissions;
}
