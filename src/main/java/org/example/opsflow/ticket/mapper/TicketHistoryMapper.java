package org.example.opsflow.ticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.opsflow.ticket.entity.TicketHistory;


@Mapper
public interface TicketHistoryMapper {
    int insert(TicketHistory ticketHistory);
}




