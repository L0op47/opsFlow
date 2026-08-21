package org.example.opsflow.ticket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketResponse;
import org.example.opsflow.ticket.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ApiResponse<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            Authentication authentication){
        TicketResponse response = ticketService.createTicket(request,authentication.getName());
        return ApiResponse.success(response);
    }
}
