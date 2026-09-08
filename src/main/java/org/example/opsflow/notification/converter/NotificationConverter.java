package org.example.opsflow.notification.converter;

import org.example.opsflow.notification.dto.NotificationResponse;
import org.example.opsflow.notification.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface NotificationConverter {

//    NotificationResponse toNotificationResponse(
//            Notification notification
//    );

    List<NotificationResponse> toNotificationResponseList(
            List<Notification> notifications
    );
}