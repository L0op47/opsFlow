package org.example.opsflow.notification.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.common.exception.ErrorCode;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.notification.converter.NotificationConverter;
import org.example.opsflow.notification.dto.NotificationResponse;
import org.example.opsflow.notification.entity.Notification;
import org.example.opsflow.notification.mapper.NotificationMapper;
import org.example.opsflow.notification.service.NotificationService;
import org.example.opsflow.user.entity.User;
import org.example.opsflow.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;
    private final UserService userService;
    private final NotificationConverter notificationConverter;

    @Override
    public PageResponse<NotificationResponse> getMyNotifications(String username, int page, int size, Integer readStatus) {
        if(page < 1){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"页码必须大于等于1");

        }
        if(size < 1 || size > 100){
            throw new BusinessException(ErrorCode.INVALID_PAGE_PARAMETER,"每页的数量必须在1-100之间");
        }
        if (readStatus != null && readStatus != 0 && readStatus != 1) {
            throw new BusinessException(
                    ErrorCode.INVALID_NOTIFICATION_READ_STATUS,
                    "通知已读状态只能是0或者1"
            );
        }
        User currentUser = userService.getActiveUser(username);

        PageHelper.startPage(page,size);
        List<Notification> notifications = notificationMapper.findByRecipientUserId(currentUser.getId(),readStatus);
        PageInfo<Notification> pageInfo = new PageInfo<>(notifications);
        List<NotificationResponse> records = notificationConverter.toNotificationResponseList(notifications);
        return new PageResponse<>(
                records,
                pageInfo.getTotal(),
                page,
                size
        );
    }

    @Override
    @Transactional
    public void markAsRead(String username, Long id) {
        User currentUser = userService.getActiveUser(username);
        Notification notification = notificationMapper.findByIdAndRecipientUserId(id,currentUser.getId());
        if(notification == null){
            throw new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(Objects.equals(notification.getReadStatus(),1)){
            return;
        }
        int affectedRows = notificationMapper.markAsRead(id,currentUser.getId());
        if(affectedRows != 1){
            throw new BusinessException(ErrorCode.DATABASE_OPERATION_FAILED,"通知标记已读失败");
        }
    }
}
