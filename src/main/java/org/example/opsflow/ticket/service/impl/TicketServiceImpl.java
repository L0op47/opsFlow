package org.example.opsflow.ticket.service.impl;

import lombok.AllArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketResponse;
import org.example.opsflow.ticket.entity.Ticket;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;
import org.example.opsflow.ticket.mapper.TicketMapper;
import org.example.opsflow.ticket.service.TicketService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import static org.example.opsflow.ticket.converter.TicketConverter.toTicketResponse;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;
    @Override
    public TicketResponse createTicket(CreateTicketRequest request, String name) {
        User creator = userMapper.findByUsername(name);
        if(creator == null){
            throw new BusinessException(40006,"当前用户不存在");
        }
        if (creator.getStatus() == 0){
            throw new BusinessException(40009,"当前用户已被禁用");
        }

        String title = request.getTitle().trim();
        String description = request.getDescription().trim();
        String category = request.getCategory()
                .trim()
                .toUpperCase(Locale.ROOT);
        TicketPriority priority = request.getPriority() == null
                ? TicketPriority.MEDIUM
                : request.getPriority();

        Ticket ticket = new Ticket();
        ticket.setTicketNo(generateTicketNo());
        ticket.setCategory(category);
        ticket.setDescription(description);
        ticket.setTitle(title);
        ticket.setPriority(priority);
        ticket.setStatus(TicketStatus.PENDING);
        ticket.setDepartmentId(creator.getDepartmentId());
        LocalDateTime now = LocalDateTime.now().withNano(0);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        ticket.setCreatorId(creator.getId());
        int affectedRows = ticketMapper.insert(ticket);
        if (affectedRows != 1){
            throw new BusinessException(50003,"工单创建失败");
        }
        return toTicketResponse(ticket);
    }

    private String generateTicketNo(){
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        String randomPart = UUID.randomUUID()
                                .toString()
                                .replace("-","")
                                .substring(0,20)
                                .toLowerCase();
        return "TK" + date + randomPart;
    }
}
