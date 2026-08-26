package org.example.opsflow.ticket.converter;


import org.example.opsflow.ticket.dto.TicketDetailResponse;
import org.example.opsflow.ticket.dto.TicketHistoryResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;
import org.example.opsflow.ticket.entity.Ticket;
import org.example.opsflow.ticket.entity.TicketHistory;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TicketConverter {
    TicketSummaryResponse toSummaryResponse(Ticket ticket);

    TicketDetailResponse toDetailResponse(Ticket ticket);

    List<TicketSummaryResponse> toSummaryResponseList(List<Ticket> tickets);

    List<TicketHistoryResponse> toHistoryResponseList(List<TicketHistory> TicketHistories);
}
