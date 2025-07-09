package com.mddev.bookstore.orders.jobs;

import com.mddev.bookstore.orders.domain.OrderService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingJob {

  private final OrderService orderService;

  @Scheduled(cron = "${orders.new-orders-job-cron}")
  @SchedulerLock(name = "processNewOrders")
  public void processNewOrders() {
    LockAssert.assertLocked();
    log.info("Processing new orders at {}", Instant.now());
    orderService.processNewOrders();
  }
}
