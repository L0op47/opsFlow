package org.example.opsflow.ticket.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketDetailResponse;
import org.example.opsflow.ticket.dto.TicketHistoryResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;
import org.example.opsflow.ticket.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    @PreAuthorize("hasAuthority('ticket:create')")
    @PostMapping
    public ApiResponse<TicketDetailResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            Authentication authentication){
        TicketDetailResponse response = ticketService.createTicket(request,authentication.getName());
        return ApiResponse.success(response);
    }

    @PreAuthorize("hasAuthority('ticket:read:self')")
    @GetMapping("/my")
    public ApiResponse<PageResponse<TicketSummaryResponse>> getMyTickets(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){
        PageResponse<TicketSummaryResponse> response = ticketService.getMyTickets(page,size,authentication.getName());
        return ApiResponse.success(response);
    }

    @PreAuthorize("hasAuthority('ticket:read:self')")
    @GetMapping("/{id}")
    public ApiResponse<TicketDetailResponse> getTicket(
            @PathVariable Long id,
            Authentication authentication){
        TicketDetailResponse response = ticketService.getTicketDetail(id,authentication.getName());
        return ApiResponse.success(response);
    }

    @PreAuthorize("hasAuthority('ticket:read:pending')")
    @GetMapping
    public ApiResponse<PageResponse<TicketSummaryResponse>> getPendingTickets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageResponse<TicketSummaryResponse> response = ticketService.getPendingTickets(page,size);
        return ApiResponse.success(response);
    }

    @PreAuthorize("hasAuthority('ticket:accept')")
    @PostMapping("/{id}/accept")
    public ApiResponse<Void> acceptTicket(
            @PathVariable Long id,
            Authentication authentication
    ){
        ticketService.acceptTicket(id,authentication.getName());
        return ApiResponse.success();
    }

    @PreAuthorize("hasAuthority('ticket:resolve')")
    @GetMapping("/assigned-to-me")
    public  ApiResponse<PageResponse<TicketSummaryResponse>> getAssignedToMe(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageResponse<TicketSummaryResponse> response = ticketService.getMyProcessingTickets(authentication.getName(),page,size);
        return ApiResponse.success(response);
    }

    @PreAuthorize("hasAuthority('ticket:resolve')")
    @PostMapping("/{id}/resolve")
    public ApiResponse<Void> resolveTicket(
            @PathVariable Long id,
            Authentication authentication
    ){
        ticketService.resolveTicket(id,authentication.getName());
        return ApiResponse.success();
    }

    @PreAuthorize("hasAuthority('ticket:close')")
    @PostMapping("/{id}/close")
    public ApiResponse<Void> closeTicket(
            @PathVariable Long id,
            Authentication authentication
    ){
        ticketService.closeTicket(id,authentication.getName());
        return ApiResponse.success();
    }

    @PreAuthorize("hasAuthority('ticket:cancel')")
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelTicket(
            @PathVariable Long id,
            Authentication authentication
    ){
        ticketService.cancelTicket(id,authentication.getName());
        return ApiResponse.success();
    }

    @PreAuthorize("hasAuthority('ticket:read:self')")
    @GetMapping("/{id}/history")
    public  ApiResponse<List<TicketHistoryResponse>> getTicketHistory(
            @PathVariable Long id,
            Authentication authentication
    ){
        List<TicketHistoryResponse> responses = ticketService.getTicketHistory(id,authentication.getName());
        return ApiResponse.success(responses);
    }
}
