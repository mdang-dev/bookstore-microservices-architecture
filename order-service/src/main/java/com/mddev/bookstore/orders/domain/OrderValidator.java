package com.mddev.bookstore.orders.domain;

import com.mddev.bookstore.orders.clients.catalog.Product;
import com.mddev.bookstore.orders.clients.catalog.ProductServiceClient;
import com.mddev.bookstore.orders.domain.models.CreateOrderRequest;
import com.mddev.bookstore.orders.domain.models.OrderItem;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class OrderValidator {

  private final ProductServiceClient client;

  void validate(CreateOrderRequest request) {
    Set<OrderItem> items = request.items();
    for (OrderItem item : items) {
      Product product =
          client
              .getProductByCode(item.code())
              .orElseThrow(() -> new InvalidOrderException("Invalid Product code: " + item.code()));
      if (item.price().compareTo(product.price()) != 0) {
        log.error(
            "Product price not matching. Actual price:{}, received price:{}",
            product.price(),
            item.price());
        throw new InvalidOrderException("Product price not matching");
      }
    }
  }
}
