package org.example.opsflow.notification.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.opsflow.config.RabbitMqConfig;
import org.example.opsflow.notification.service.NotificationService;
import org.example.opsflow.ticket.event.TicketStatusChangedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketNotificationConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMqConfig.NOTIFICATION_TICKET_QUEUE)
    public void consume(TicketStatusChangedEvent event) {
        log.debug("收到工单状态事件{}",event);
        notificationService.createFromTicketStatusChangedEvent(event);
    }
}
