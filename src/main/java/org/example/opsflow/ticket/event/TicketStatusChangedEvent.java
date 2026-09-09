package org.example.opsflow.ticket.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.opsflow.ticket.enums.TicketStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketStatusChangedEvent {

    private String eventId;

    private Long ticketId;

    private String ticketNo;

    private String ticketTitle;

    private TicketStatus fromStatus;

    private TicketStatus toStatus;

    private Long operatorUserId;

    private Long recipientUserId;

    private LocalDateTime occurredAt;
}