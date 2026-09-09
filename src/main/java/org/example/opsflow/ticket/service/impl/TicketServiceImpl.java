package org.example.opsflow.ticket.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.asset.entity.Asset;
import org.example.opsflow.asset.mapper.AssetMapper;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.ticket.dto.*;
import org.example.opsflow.ticket.entity.Ticket;
import org.example.opsflow.ticket.entity.TicketHistory;
import org.example.opsflow.ticket.enums.TicketAction;
import org.example.opsflow.ticket.enums.TicketPriority;
import org.example.opsflow.ticket.enums.TicketStatus;
import org.example.opsflow.ticket.event.TicketStatusChangedEvent;
import org.example.opsflow.ticket.mapper.TicketHistoryMapper;
import org.example.opsflow.ticket.mapper.TicketMapper;
import org.example.opsflow.ticket.messaging.TicketEventPublisher;
import org.example.opsflow.ticket.service.TicketService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.service.UserService;
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
    private final UserService userService;
    private final TicketHistoryMapper ticketHistoryMapper;
    private final TicketEventPublisher ticketEventPublisher;
    private final AssetMapper assetMapper;

    @Override
    public TicketDetailResponse createTicket(CreateTicketRequest request, String name) {
        User currentUser = userService.getActiveUser(name);
        Asset asset = assetMapper.findById(request.getAssetId());
        if(asset == null){
            throw new BusinessException(ErrorCode.ASSET_NOT_FOUND);
        }
        if(!Objects.equals(asset.getStatus(),1)) {
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
        ticket.setCreatorId(currentUser.getId());
        ticket.setDeadlineAt(calculateDeadline(priority));
        ticket.setAssetId(asset.getId());
        int affectedRows = ticketMapper.insert(ticket);
        if (affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"工单创建失败");
        }
        return ticketMapper.findDetailResponseById(ticket.getId());
    }

    @Override
    public PageResponse<TicketSummaryResponse> getMyTickets(int page, int size, String name) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        User currentUser = userService.getActiveUser(name);

        PageHelper.startPage(page,size);
        List<TicketSummaryResponse> records =
                ticketMapper.findSummariesByCreatorId(currentUser.getId());
        PageInfo<TicketSummaryResponse> pageInfo = new PageInfo<>(records);
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );
    }

    @Override
    public TicketDetailResponse getTicketDetail(Long id, String name) {
        User currentUser = userService.getActiveUser(name);
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(!Objects.equals(ticket.getCreatorId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED);
        }
        TicketDetailResponse response = ticketMapper.findDetailResponseById(id);
        if (response == null) {
            throw new BusinessException(
                    ErrorCode.DATABASE_OPERATION_FAILED,
                    "工单详情查询失败"
            );
        }
        return response;
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
        List<TicketSummaryResponse> records = ticketMapper.findPendingSummaries();
        PageInfo<TicketSummaryResponse> pageInfo = new PageInfo<>(records);
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
        User currentUser = userService.getActiveUser(name);
        PageHelper.startPage(page,size);
        List<TicketSummaryResponse> records =
                ticketMapper.findProcessingSummariesByAssigneeId(currentUser.getId());
        PageInfo<TicketSummaryResponse> pageInfo = new PageInfo<>(records);
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
        User currentUser = userService.getActiveUser(name);
        int affectedRows = ticketMapper.acceptTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.ACCEPT,
                    TicketStatus.PENDING,TicketStatus.PROCESSING);
            Ticket ticket = ticketMapper.findById(id);

            publishStatusChangeEvent(
                    ticket,
                    TicketStatus.PENDING,
                    TicketStatus.PROCESSING,
                    currentUser.getId(),
                    ticket.getCreatorId()
            );

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
        User currentUser = userService.getActiveUser(name);
        int affectedRows = ticketMapper.resolveTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.RESOLVE,
                    TicketStatus.PROCESSING,TicketStatus.RESOLVED);
            Ticket ticket = ticketMapper.findById(id);

            publishStatusChangeEvent(
                    ticket,
                    TicketStatus.PROCESSING,
                    TicketStatus.RESOLVED,
                    currentUser.getId(),
                    ticket.getCreatorId()
            );
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
        User currentUser = userService.getActiveUser(name);
        int affectedRows = ticketMapper.closeTicket(id,currentUser.getId());
        if (affectedRows == 1) {
            recordTicketHistory(id,currentUser.getId(),TicketAction.CLOSE,
                    TicketStatus.RESOLVED,TicketStatus.CLOSED);
            Ticket ticket = ticketMapper.findById(id);

            publishStatusChangeEvent(
                    ticket,
                    TicketStatus.RESOLVED,
                    TicketStatus.CLOSED,
                    currentUser.getId(),
                    ticket.getAssigneeId()
            );
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
        User currentUser = userService.getActiveUser(name);
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
        User currentUser = userService.getActiveUser(name);
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if(!Objects.equals(ticket.getCreatorId(), currentUser.getId())){
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED);
        }
        return ticketHistoryMapper.findResponsesByTicketId(id);
    }

    @Override
    public TicketDetailResponse updateTicket(Long id, UpdateTicketRequest request, String name) {
        User user =  userService.getActiveUser(name);
        Ticket ticket = ticketMapper.findById(id);
        if(ticket == null){
            throw new BusinessException(ErrorCode.TICKET_NOT_FOUND);
        }
        if (!Objects.equals(ticket.getCreatorId(), user.getId())) {
            throw new BusinessException(ErrorCode.TICKET_ACCESS_DENIED);
        }
        if(!Objects.equals(ticket.getStatus(), TicketStatus.PENDING)){
            throw new BusinessException(ErrorCode.INVALID_TICKET_STATUS_TRANSITION);
        }
        Asset asset = assetMapper.findById(request.getAssetId());
        if(asset == null){
            throw new BusinessException(ErrorCode.ASSET_NOT_FOUND);
        }
        if(!Objects.equals(asset.getStatus(),1)) {
            throw new BusinessException(ErrorCode.ACCOUNT_DISABLED);
        }
        String title = request.getTitle().trim();
        String description = request.getDescription().trim();
        String category = request.getCategory()
                .trim()
                .toUpperCase(Locale.ROOT);
        TicketPriority priority = request.getPriority() == null
                ? ticket.getPriority()
                : request.getPriority();
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setCategory(category);
        ticket.setPriority(priority);
        ticket.setDeadlineAt(calculateDeadline(priority));
        ticket.setAssetId(asset.getId());
        int affectedRows = ticketMapper.updatePendingTicket(ticket,user.getId());
        if (affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"工单修改失败");
        }
        return ticketMapper.findDetailResponseById(id);
    }

    @Override
    public PageResponse<TicketSummaryResponse> getOverdueTickets(int page, int size) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        PageHelper.startPage(page,size);
        List<TicketSummaryResponse> records = ticketMapper.findOverdueSummaries();
        PageInfo<TicketSummaryResponse> pageInfo = new PageInfo<>(records);
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );
    }

    @Override
    public Long countOverdueTickets() {
        return  ticketMapper.countOverdueTickets();
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

    private LocalDateTime calculateDeadline(TicketPriority priority){
        LocalDateTime now = LocalDateTime.now();
        return switch (priority){
            case LOW -> now.plusHours(72);
            case MEDIUM -> now.plusHours(48);
            case HIGH -> now.plusHours(24);
            case URGENT -> now.plusHours(4);
        };
    }

    private void publishStatusChangeEvent(
            Ticket ticket,
            TicketStatus fromStatus,
            TicketStatus toStatus,
            Long operatorUserId,
            Long recipientUserId){
        TicketStatusChangedEvent event = TicketStatusChangedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .ticketId(ticket.getId())
                .ticketNo(ticket.getTicketNo())
                .ticketTitle(ticket.getTitle())
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .operatorUserId(operatorUserId)
                .recipientUserId(recipientUserId)
                .occurredAt(LocalDateTime.now())
                .build();
        ticketEventPublisher.publishStatusChanged(event);
    }
}
