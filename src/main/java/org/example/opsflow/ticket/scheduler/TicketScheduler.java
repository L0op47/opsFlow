package org.example.opsflow.ticket.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.opsflow.ticket.service.TicketService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TicketScheduler {
    private final TicketService ticketService;

    @Scheduled(
            fixedDelayString = "${opsflow.ticket.sla.scan-delay-ms}"
    )
    public void scanOverdueTickets(){
        long count = ticketService.countOverdueTickets();
        if(count > 0){
            log.warn("发现超时工单，count={}", count);

        }else {
            log.info("SLA扫描完成，未发现超时工单");
        }
    }
}
