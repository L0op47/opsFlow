package org.example.opsflow.asset.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssetResponse {
    private Long id;

    private String assetNo;

    private String name;

    private String category;

    private String model;

    private Long assignedUserId;

//    private String assignedUserName;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
