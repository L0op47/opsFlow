package org.example.opsflow.ticket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.ticket.converter.TicketConverter;
import org.example.opsflow.ticket.dto.CreateTicketRequest;
import org.example.opsflow.ticket.dto.TicketDetailResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;
    private final TicketConverter ticketConverter;

    @Override
    public TicketDetailResponse createTicket(CreateTicketRequest request, String name) {
        User creator = userMapper.findByUsername(name);
        if(creator == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (creator.getStatus() == 0){
            throw new BusinessException(ErrorCode.INVALID_USER_STATUS);
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
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"工单创建失败");
        }
        return ticketConverter.toDetailResponse(ticket);
    }

    @Override
    public PageResponse<TicketSummaryResponse> getMyTickets(int page, int size, String name) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        User user = userMapper.findByUsername(name);
        if(user == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        PageHelper.startPage(page,size);
        List<Ticket> tickets = ticketMapper.findByCreatorId(user.getId());
        PageInfo<Ticket> pageInfo = new PageInfo<>(tickets);
        List<TicketSummaryResponse> records = ticketConverter.toSummaryResponseList(tickets);
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );
    }

    @Override
    public TicketDetailResponse getTicketDetail(Long id, String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if(currentUser.getStatus() == 0){
            throw new BusinessException(ErrorCode.INVALID_USER_STATUS);
        }

        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(!Objects.equals(ticket.getCreatorId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED);
        }
        return ticketConverter.toDetailResponse(ticket);
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
