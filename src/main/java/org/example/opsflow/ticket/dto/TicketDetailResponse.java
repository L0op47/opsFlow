package org.example.opsflow.ticket.dto;

import lombok.Data;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
public class TicketDetailResponse {
    private Long id;
    private String ticketNo;
    private String title;
    private String description;
    private String category;
    private TicketPriority priority;
    private TicketStatus status;
    private Long creatorId;
    private Long assigneeId;
    private Long departmentId;
    private Long assetId;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
    private LocalDateTime deadlineAt;
    private LocalDateTime updatedAt;
}
