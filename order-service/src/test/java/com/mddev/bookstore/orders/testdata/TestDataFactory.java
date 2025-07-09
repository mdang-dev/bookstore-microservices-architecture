package com.mddev.bookstore.orders.testdata;

import static org.instancio.Select.field;

import com.mddev.bookstore.orders.domain.models.Address;
import com.mddev.bookstore.orders.domain.models.CreateOrderRequest;
import com.mddev.bookstore.orders.domain.models.Customer;
import com.mddev.bookstore.orders.domain.models.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.instancio.Instancio;

public class TestDataFactory {

  static final List<String> VALID_COUNTIES = List.of("Vietnamse", "US");
  static final Set<OrderItem> VALID_ORDER_ITEMS =
      Set.of(new OrderItem("B001", "To Kill a Mockingbird", new BigDecimal("10.99"), 1));
  static final Set<OrderItem> INVALID_ORDER_ITEMS =
      Set.of(new OrderItem("ABC", "To Kill a Mockingbird", new BigDecimal("10.99"), 1));

  public static CreateOrderRequest createValidOrderRequest() {
    return Instancio.of(CreateOrderRequest.class)
        .generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@mail.com"))
        .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
        .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
        .create();
  }

  public static CreateOrderRequest createOrderRequestWithInvalidCustomer() {
    return Instancio.of(CreateOrderRequest.class)
        .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
        .set(field(Customer::phone), "")
        .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
        .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
        .create();
  }

  public static CreateOrderRequest createOrderRequestWithInvalidDeliveryAddress() {
    return Instancio.of(CreateOrderRequest.class)
        .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
        .set(field(Address::country), "")
        .set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS)
        .create();
  }

  public static CreateOrderRequest createOrderRequestWithNoItems() {
    return Instancio.of(CreateOrderRequest.class)
        .generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d#d@mail.com"))
        .generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES))
        .set(field(CreateOrderRequest::items), Set.of())
        .create();
  }
}
