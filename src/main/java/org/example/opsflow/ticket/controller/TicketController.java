package org.example.opsflow.ticket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.operationlog.annotation.OperationLog;
import org.example.opsflow.ticket.dto.*;
import org.example.opsflow.ticket.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(
        name = "工单管理",
        description = "工单创建、查询、状态流转和 SLA 管理"
)
public class TicketController {
    private final TicketService ticketService;

    @OperationLog(
            module = "TICKET",
            action = "CREATE"
    )
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

    @OperationLog(
            module = "TICKET",
            action = "ACCEPT",
            targetIdArg = 0
    )
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

    @OperationLog(
            module = "TICKET",
            action = "RESOLVE",
            targetIdArg = 0
    )
    @PreAuthorize("hasAuthority('ticket:resolve')")
    @PostMapping("/{id}/resolve")
    public ApiResponse<Void> resolveTicket(
            @PathVariable Long id,
            Authentication authentication
    ){
        ticketService.resolveTicket(id,authentication.getName());
        return ApiResponse.success();
    }

    @OperationLog(
            module = "TICKET",
            action = "CLOSE",
            targetIdArg = 0
    )
    @PreAuthorize("hasAuthority('ticket:close')")
    @PostMapping("/{id}/close")
    public ApiResponse<Void> closeTicket(
            @PathVariable Long id,
            Authentication authentication
    ){
        ticketService.closeTicket(id,authentication.getName());
        return ApiResponse.success();
    }

    @OperationLog(
            module = "TICKET",
            action = "CANCEL",
            targetIdArg = 0
    )
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

    @OperationLog(
            module = "TICKET",
            action = "UPDATE",
            targetIdArg = 0
    )
    @PreAuthorize("hasAuthority('ticket:create')")
    @PutMapping("/{id}")
    public ApiResponse<TicketDetailResponse>  updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request,
            Authentication authentication
    ){
        return ApiResponse.success(ticketService.updateTicket(id,request,authentication.getName()));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('ticket:read:pending')")
    @Operation(
            summary = "查询超时工单",
            description = "分页查询已超过截止时间，且状态为 PENDING 或 PROCESSING 的工单"
    )
    public ApiResponse<PageResponse<TicketSummaryResponse>> getOverdueTickets(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ApiResponse.success(ticketService.getOverdueTickets(page,size));
    }
}
