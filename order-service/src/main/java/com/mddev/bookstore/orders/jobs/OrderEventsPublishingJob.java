package com.mddev.bookstore.orders.jobs;

import com.mddev.bookstore.orders.domain.OrderEventService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class OrderEventsPublishingJob {

  private final OrderEventService orderEventService;

  @Scheduled(cron = "${orders.publish-order-events-job-cron}")
  public void publishOrderEvents() {
    log.info("Publishing Order Events at {}", Instant.now());
    orderEventService.publishOrderEvents();
  }
}
