package org.example.opsflow.ticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.opsflow.ticket.entity.Ticket;

@Mapper
public interface TicketMapper {
    int insert(Ticket ticket);
}
