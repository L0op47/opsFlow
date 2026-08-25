package org.example.opsflow.ticket.service;

import jakarta.validation.Valid;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketDetailResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;

public interface TicketService {
    TicketDetailResponse createTicket(@Valid CreateTicketRequest request, String name);

    PageResponse<TicketSummaryResponse> getMyTickets(int page, int size, String name);

    TicketDetailResponse getTicketDetail(Long id, String name);

    PageResponse<TicketSummaryResponse> getPendingTickets(int page, int size);

    void acceptTicket(Long id, String name);
}
