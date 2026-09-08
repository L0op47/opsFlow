package org.example.opsflow.notification.entity;

import java.time.LocalDateTime;
import lombok.Data;


@Data
public class Notification {
    private Long id;

    private String eventId;

    private Long recipientUserId;

    private String type;

    private String title;

    private String content;

    private String businessType;

    private Long businessId;

    private Integer readStatus;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}