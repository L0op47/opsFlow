package org.example.opsflow.notification.service;

import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.notification.dto.NotificationResponse;

public interface NotificationService {
    PageResponse<NotificationResponse> getMyNotifications(String username, int page, int size, Integer readStatus);

    void markAsRead(String username,Long id);
}
