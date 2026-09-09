package org.example.opsflow.notification.controller;

import lombok.RequiredArgsConstructor;
import org.example.opsflow.common.response.ApiResponse;
import org.example.opsflow.common.response.PageResponse;
import org.example.opsflow.notification.dto.NotificationResponse;
import org.example.opsflow.notification.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/my")
    public ApiResponse<PageResponse<NotificationResponse>> getMyNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer readStatus){
        return ApiResponse.success( notificationService.getMyNotifications(authentication.getName(),page,size,readStatus));
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(
            Authentication authentication,
            @PathVariable Long id
    ){
        notificationService.markAsRead(authentication.getName(),id);
        return ApiResponse.success();
    }


}
