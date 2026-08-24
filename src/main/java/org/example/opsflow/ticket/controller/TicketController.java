package org.example.opsflow.ticket.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketDetailResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;
import org.example.opsflow.ticket.service.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ApiResponse<TicketDetailResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            Authentication authentication){
        TicketDetailResponse response = ticketService.createTicket(request,authentication.getName());
        return ApiResponse.success(response);
    }

    @GetMapping("/my")
    public ApiResponse<PageResponse<TicketSummaryResponse>> getMyTickets(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        PageResponse<TicketSummaryResponse> tickets = ticketService.getMyTickets(page,size,authentication.getName());
        return ApiResponse.success(tickets);
    }


//    @GetMapping("/{id}")
//    public ApiResponse<TicketDetailResponse> getTicket(@PathVariable Long id){
//
//    }
}
