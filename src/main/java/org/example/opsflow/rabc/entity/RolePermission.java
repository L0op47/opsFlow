package org.example.opsflow.rabc.entity;

import java.time.LocalDateTime;
import lombok.Data;


@Data
public class RolePermission {
    private Long id;

    private Long permissionId;

    private Long roleId;

    private LocalDateTime createdAt;
}