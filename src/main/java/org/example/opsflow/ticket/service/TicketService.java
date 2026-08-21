package org.example.opsflow.ticket.service;

import jakarta.validation.Valid;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketResponse;

public interface TicketService {
    TicketResponse createTicket(@Valid CreateTicketRequest request, String name);
}
