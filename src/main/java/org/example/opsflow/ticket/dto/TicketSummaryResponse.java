package org.example.opsflow.ticket.dto;

import lombok.Data;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
public class TicketSummaryResponse {
    private Long id;
    private String ticketNo;
    private String title;
    private String category;
    private TicketPriority priority;
    private TicketStatus status;

    private Long creatorId;
    private String creatorName;

    private Long assigneeId;
    private String assigneeName;

    private Long departmentId;
    private String departmentName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deadlineAt;
    private boolean overdue;
}
