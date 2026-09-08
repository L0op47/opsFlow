package org.example.opsflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class RabbitMqConfig {
    public static final String TICKET_EVENT_EXCHANGE = "opsflow.ticket.event.exchange";

    public static final String NOTIFICATION_TICKET_QUEUE =
            "opsflow.notification.ticket.queue";

    public static final String TICKET_STATUS_ROUTING_PATTERN =
            "ticket.status.*";

    public static final String NOTIFICATION_DEAD_LETTER_EXCHANGE =
            "opsflow.notification.dlx";

    public static final String NOTIFICATION_DEAD_LETTER_QUEUE =
            "opsflow.notification.dlq";

    public static final String NOTIFICATION_DEAD_LETTER_ROUTING_KEY =
            "notification.ticket.dead";

    @Bean
    public TopicExchange ticketEventExchange(){
        return new TopicExchange(TICKET_EVENT_EXCHANGE,true,false);
    }

    @Bean
    public Queue notificationTicketQueue(){
        return QueueBuilder
                .durable(NOTIFICATION_TICKET_QUEUE)
                .deadLetterExchange(NOTIFICATION_DEAD_LETTER_EXCHANGE)
                .deadLetterRoutingKey(NOTIFICATION_DEAD_LETTER_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding notificationTicketBinding(
            @Qualifier("notificationTicketQueue") Queue queue,
            @Qualifier("ticketEventExchange") TopicExchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(TICKET_STATUS_ROUTING_PATTERN);

    }

    @Bean
    public DirectExchange notificationDeadLetterExchange() {
        return new DirectExchange(
                NOTIFICATION_DEAD_LETTER_EXCHANGE,
                true,
                false
        );
    }

    @Bean
    public Queue notificationDeadLetterQueue() {
        return QueueBuilder
                .durable(NOTIFICATION_DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public Binding notificationDeadLetterBinding(
            @Qualifier("notificationDeadLetterQueue") Queue queue,
            @Qualifier("notificationDeadLetterExchange")
            DirectExchange exchange) {

        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(NOTIFICATION_DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter rabbitMessageConverter(ObjectMapper objectMapper){
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
