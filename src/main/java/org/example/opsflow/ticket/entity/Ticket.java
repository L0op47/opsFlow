package org.example.opsflow.ticket.entity;

import lombok.Data;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
public class Ticket {
    Long id;
    String ticketNo;
    String title;
    String description;
    String category;
    TicketStatus status;
    TicketPriority priority;
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
