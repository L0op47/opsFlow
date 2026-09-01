package org.example.opsflow.asset.entity;

import java.time.LocalDateTime;
import lombok.Data;


@Data
public class Asset {

    private Long id;

    private String assetNo;

    private String name;

    private String category;

    private String model;

    private Long assignedUserId;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}