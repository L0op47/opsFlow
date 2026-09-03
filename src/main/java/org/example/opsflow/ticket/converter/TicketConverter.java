package org.example.opsflow.ticket.converter;


import org.example.opsflow.ticket.dto.TicketDetailResponse;
import org.example.opsflow.ticket.dto.TicketHistoryResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;
import org.example.opsflow.ticket.entity.Ticket;
import org.example.opsflow.ticket.entity.TicketHistory;
import org.example.opsflow.ticket.enums.TicketStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TicketConverter {
    @Mapping(target = "overdue",expression = "java(isOverdue(ticket))")
    TicketSummaryResponse toSummaryResponse(Ticket ticket);

    @Mapping(target = "overdue",expression = "java(isOverdue(ticket))")
    TicketDetailResponse toDetailResponse(Ticket ticket);

    List<TicketSummaryResponse> toSummaryResponseList(List<Ticket> tickets);

    List<TicketHistoryResponse> toHistoryResponseList(List<TicketHistory> TicketHistories);

    default boolean isOverdue(Ticket ticket) {
        if (ticket.getDeadlineAt() == null) {
            return false;
        }

        boolean unfinished =
                ticket.getStatus() == TicketStatus.PENDING
                        || ticket.getStatus() == TicketStatus.PROCESSING;

        return unfinished
                && LocalDateTime.now().isAfter(ticket.getDeadlineAt());
    }
}
