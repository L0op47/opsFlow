package org.example.opsflow.ticket.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.opsflow.ticket.entity.Ticket;

import java.util.List;

@Mapper
public interface TicketMapper {
    int insert(Ticket ticket);

    List<Ticket> findByCreatorId(@Param("creatorId") Long creatorId);

    Ticket findById(@Param("id") Long id);

    List<Ticket> findPendingTickets();

    int acceptTicket(@Param("id")Long id, @Param("assigneeId") Long assigneeId);

    List<Ticket> findProcessingByAssigneeId(@Param("assigneeId") Long assigneeId);

    int resolveTicket(@Param("id") Long id,@Param("assigneeId") Long id1);
}
