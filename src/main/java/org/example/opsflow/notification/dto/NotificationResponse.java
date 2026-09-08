package org.example.opsflow.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {

    private Long id;

    private String type;

    private String title;

    private String content;

    private String businessType;

    private Long businessId;

    private Integer readStatus;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}