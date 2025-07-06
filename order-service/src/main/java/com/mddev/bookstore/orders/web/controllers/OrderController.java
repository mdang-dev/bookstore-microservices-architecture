package com.mddev.bookstore.orders.web.controllers;

import com.mddev.bookstore.orders.domain.OrderNotFoundException;
import com.mddev.bookstore.orders.domain.OrderService;
import com.mddev.bookstore.orders.domain.SecurityService;
import com.mddev.bookstore.orders.domain.models.CreateOrderRequest;
import com.mddev.bookstore.orders.domain.models.CreateOrderResponse;
import com.mddev.bookstore.orders.domain.models.OrderDTO;
import com.mddev.bookstore.orders.domain.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;
  private final SecurityService securityService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
    String username = securityService.getLoginUsername();
    log.info("Creating order for user: {}", username);
    return orderService.createOrder(username, request);
  }

  @GetMapping
  List<OrderSummary> getOrders() {
    String username = securityService.getLoginUsername();
    log.info("Fetching orders for user: {}", username);
    return orderService.findOrders(username);
  }

  @GetMapping("/{orderNumber}")
  OrderDTO getOrder(@PathVariable String orderNumber) {
    log.info("Fetching order by id: {}", orderNumber);
    String username = securityService.getLoginUsername();
    return orderService
        .findUserOrder(username, orderNumber)
        .orElseThrow(() -> new OrderNotFoundException(orderNumber));
  }
}
