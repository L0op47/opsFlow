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
import org.example.opsflow.ticket.dto.TicketHistoryResponse;
import org.example.opsflow.ticket.dto.TicketSummaryResponse;
import org.example.opsflow.ticket.entity.Ticket;
import org.example.opsflow.ticket.entity.TicketHistory;
import org.example.opsflow.ticket.enums.TicketAction;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;
import org.example.opsflow.ticket.mapper.TicketHistoryMapper;
import org.example.opsflow.ticket.mapper.TicketMapper;
import org.example.opsflow.ticket.service.TicketService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
public class  TicketServiceImpl implements TicketService {
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;
    private final TicketConverter ticketConverter;
    private final TicketHistoryMapper ticketHistoryMapper;

    @Override
    public TicketDetailResponse createTicket(CreateTicketRequest request, String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if(currentUser.getStatus() == 0){
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
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
        ticket.setDepartmentId(currentUser.getDepartmentId());
        LocalDateTime now = LocalDateTime.now().withNano(0);
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);
        ticket.setCreatorId(currentUser.getId());
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
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if(currentUser.getStatus() == 0){
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }

        PageHelper.startPage(page,size);
        List<Ticket> tickets = ticketMapper.findByCreatorId(currentUser.getId());
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
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if(currentUser.getStatus() == 0){
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
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

    @Override
    public PageResponse<TicketSummaryResponse> getPendingTickets(int page, int size) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        PageHelper.startPage(page,size);
        List<Ticket> tickets = ticketMapper.findPendingTickets();
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
    public PageResponse<TicketSummaryResponse> getMyProcessingTickets(String name, int page, int size) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!Objects.equals(currentUser.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        PageHelper.startPage(page,size);
        List<Ticket> tickets = ticketMapper.findProcessingByAssigneeId(currentUser.getId());
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
    @Transactional
    public void acceptTicket(Long id, String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!Objects.equals(currentUser.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        int affectedRows = ticketMapper.acceptTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.ACCEPT,
                    TicketStatus.PENDING,TicketStatus.PROCESSING);
            return;
        }
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        throw new BusinessException(ErrorCode.INVALID_TICKET_STATUS_TRANSITION,"工单已被接取或当前状态不允许接单");
    }

    @Override
    @Transactional
    public void resolveTicket(Long id, String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!Objects.equals(currentUser.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        int affectedRows = ticketMapper.resolveTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.RESOLVE,
                    TicketStatus.PROCESSING,TicketStatus.RESOLVED);
            return;
        }
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(ticket.getStatus() != TicketStatus.PROCESSING){
            throw new BusinessException(ErrorCode.INVALID_TICKET_STATUS_TRANSITION,"工单当前状态不允许解决");
        }
        if(!Objects.equals(ticket.getAssigneeId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED,"用户不是该工单处理人");
        }
        throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED, "工单解决失败，请稍后重试");
    }

    @Override
    @Transactional
    public void closeTicket(Long id, String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!Objects.equals(currentUser.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        int affectedRows = ticketMapper.closeTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.CLOSE,
                    TicketStatus.RESOLVED,TicketStatus.CLOSED);
            return;
        }
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(ticket.getStatus() != TicketStatus.RESOLVED){
            throw new BusinessException(ErrorCode.INVALID_TICKET_STATUS_TRANSITION,"工单当前状态不允许关闭");
        }
        if(!Objects.equals(ticket.getCreatorId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED,"用户不是该工单创建人");
        }
        throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED, "工单关闭失败，请稍后重试");
    }

    @Override
    @Transactional
    public void cancelTicket(Long id, String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!Objects.equals(currentUser.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        int affectedRows = ticketMapper.cancelTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.CANCEL,
                    TicketStatus.PENDING,TicketStatus.CANCELED);
            return;
        }
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(ticket.getStatus() != TicketStatus.PENDING){
            throw new BusinessException(ErrorCode.INVALID_TICKET_STATUS_TRANSITION,"工单当前状态不允许取消");
        }
        if(!Objects.equals(ticket.getCreatorId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED,"用户不是该工单创建人");
        }
        throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED, "工单取消失败，请稍后重试");
    }

    @Override
    public List<TicketHistoryResponse> getTicketHistory(Long id,String name) {
        User currentUser = userMapper.findByUsername(name);
        if(currentUser == null){
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        if (!Objects.equals(currentUser.getStatus(), 1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(!Objects.equals(ticket.getCreatorId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED);
        }
        List<TicketHistory> ticketHistories = ticketHistoryMapper.findByTicketId(id);
        return ticketConverter.toHistoryResponseList(ticketHistories);
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

    private void recordTicketHistory(Long id,Long userId,TicketAction action,TicketStatus fromStatus,TicketStatus toStatus){
        TicketHistory ticketHistory = new TicketHistory();
        ticketHistory.setTicketId(id);
        ticketHistory.setOperatorId(userId);
        ticketHistory.setAction(action);
        ticketHistory.setFromStatus(fromStatus);
        ticketHistory.setToStatus(toStatus);
        ticketHistory.setRemark(null);
        int historyAffectedRows = ticketHistoryMapper.insert(ticketHistory);
        if(historyAffectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED);
        }
    }
}
