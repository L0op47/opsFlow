package org.example.opsflow.ticket.dto;

import lombok.Data;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
public class TicketDetailResponse {
    Long id;
    String ticketNo;
    String title;
    String description;
    String category;
    TicketPriority priority;
    TicketStatus status;
    Long creatorId;
    Long assigneeId;
    Long departmentId;
    Long assetId;
    LocalDateTime createdAt;
    LocalDateTime acceptedAt;
    LocalDateTime resolvedAt;
    LocalDateTime closedAt;
    LocalDateTime deadlineAt;
    LocalDateTime updatedAt;
}
