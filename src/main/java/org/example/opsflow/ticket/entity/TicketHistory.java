package org.example.opsflow.ticket.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.example.opsflow.ticket.enums.TicketAction;
import org.example.opsflow.ticket.enums.TicketStatus;

@Data
public class TicketHistory {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Long ticketId;

    /**
     * 
     */
    private Long operatorId;

    /**
     * 
     */
    private TicketAction action;

    /**
     * 
     */
    private TicketStatus fromStatus;

    /**
     * 
     */
    private TicketStatus toStatus;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private LocalDateTime createdAt;
}