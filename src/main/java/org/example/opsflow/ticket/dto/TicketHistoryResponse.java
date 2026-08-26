package org.example.opsflow.ticket.dto;

import lombok.Data;
import org.example.opsflow.ticket.enums.TicketAction;
import org.example.opsflow.ticket.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
public class TicketHistoryResponse {
    private Long id;

    private Long ticketId;

    private Long operatorId;

    private TicketAction action;

    private TicketStatus fromStatus;

    private TicketStatus toStatus;

    private String remark;

    private LocalDateTime createdAt;
}
