package org.example.opsflow.operationlog.entity;

import java.time.LocalDateTime;
import lombok.Data;


@Data
public class OperationLogRecord {
    private Long id;

    private String operatorUsername;

    private String module;

    private String action;

    private Long targetId;

    private String requestMethod;

    private String requestUri;

    private String ipAddress;

    private Integer success;

    private String errorMessage;

    private Long durationMs;

    private LocalDateTime createdAt;
}