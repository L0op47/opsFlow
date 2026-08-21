package org.example.opsflow.ticket.converter;

import org.example.opsflow.ticket.dto.TicketResponse;
import org.example.opsflow.ticket.entity.Ticket;

public final class TicketConverter {
    private TicketConverter(){}

    public static TicketResponse toTicketResponse(Ticket ticket){
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setTicketNo(ticket.getTicketNo());
        response.setTitle(ticket.getTitle());
        response.setDescription(ticket.getDescription());
        response.setCategory(ticket.getCategory());
        response.setPriority(ticket.getPriority());
        response.setStatus(ticket.getStatus());
        response.setCreatorId(ticket.getCreatorId());
        response.setAssigneeId(ticket.getAssigneeId());
        response.setDepartmentId(ticket.getDepartmentId());
        response.setAssetId(ticket.getAssetId());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setAcceptedAt(ticket.getAcceptedAt());
        response.setResolvedAt(ticket.getResolvedAt());
        response.setClosedAt(ticket.getClosedAt());
        response.setDeadlineAt(ticket.getDeadlineAt());
        response.setUpdatedAt(ticket.getUpdatedAt());
        return response;
    }
}
