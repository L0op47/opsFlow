package org.example.opsflow.ticket.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.opsflow.config.RabbitMqConfig;
import org.example.opsflow.ticket.event.TicketStatusChangedEvent;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketEventPublisher {
    private static final String ROUTING_KEY_PREFIX = "ticket.status.";

    private final RabbitTemplate rabbitTemplate;

    public void publishStatusChanged(TicketStatusChangedEvent event) {
        String routing_key = ROUTING_KEY_PREFIX + event.getToStatus().name().toLowerCase(Locale.ROOT);
        CorrelationData correlationData = new CorrelationData(event.getEventId());
        rabbitTemplate.convertAndSend(RabbitMqConfig.TICKET_EVENT_EXCHANGE,routing_key, event,correlationData);
        correlationData.getFuture().whenComplete((confirm, throwable) -> {
            if (throwable != null) {
                log.error(
                        "等待消息确认失败，eventId={}",
                        event.getEventId(),
                        throwable
                );
                return;
            }
            ReturnedMessage returned = correlationData.getReturned();

            if (returned != null) {
                log.error(
                        "消息无法路由，eventId={}, exchange={}, routingKey={}, reason={}",
                        event.getEventId(),
                        returned.getExchange(),
                        returned.getRoutingKey(),
                        returned.getReplyText()
                );
                return;
            }
            if (!confirm.isAck()) {
                log.error(
                        "RabbitMQ拒绝消息，eventId={}, reason={}",
                        event.getEventId(),
                        confirm.getReason()
                );
            } else {
                log.debug(
                        "RabbitMQ确认消息，eventId={}",
                        event.getEventId()
                );
            }
        });
    }
}
