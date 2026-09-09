package org.example.opsflow.ticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.opsflow.ticket.dto.TicketHistoryResponse;
import org.example.opsflow.ticket.entity.TicketHistory;

import java.util.List;


@Mapper
public interface TicketHistoryMapper {
    int insert(TicketHistory ticketHistory);

    List<TicketHistoryResponse> findResponsesByTicketId(
            @Param("ticketId") Long ticketId);
}




