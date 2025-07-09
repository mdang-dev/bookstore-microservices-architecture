package com.mddev.bookstore.orders.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.HttpStatus;
import com.mddev.bookstore.orders.AbstarctIT;
import com.mddev.bookstore.orders.domain.SecurityService;
import com.mddev.bookstore.orders.domain.models.OrderSummary;
import com.mddev.bookstore.orders.testdata.TestDataFactory;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-orders.sql")
class OrderControllerTest extends AbstarctIT {

  @MockitoBean SecurityService securityService;

  @BeforeEach
  void setUpTests() {
    when(securityService.getLoginUsername()).thenReturn("wok");
  }

  @Nested
  class CreateOrderTests {
    @Test
    void shouldCreateOrderSuccessfully() {
      mockGetProductByCode("B001", "To Kill a Mockingbird", new BigDecimal("10.99"));
      var payload =
          """
                          {
              "customer" : {
                  "name": "Wok",
                  "email": "Wok@gmail.com",
                  "phone": "999999999"
              },
              "deliveryAddress" : {
                  "addressLine1": "Birkelweg",
                  "addressLine2": "Hans-Edenhofer-Straße 23",
                  "city": "Berlin",
                  "state": "Berlin",
                  "zipCode": "94258",
                  "country": "Germany"
              },
              "items": [
                  {
                      "code": "B001",
                      "name": "To Kill a Mockingbird",
                      "price": 10.99,
                      "quantity": 1
                  }
              ]
          }
                              """;
      given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when()
          .post("/api/orders")
          .then()
          .statusCode(HttpStatus.SC_CREATED)
          .body("orderNumber", notNullValue());
    }

    @Test
    void shouldReturnBadRequestWhenMandatoryDataIsMissing() {
      var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
      given()
          .contentType(ContentType.JSON)
          .body(payload)
          .when()
          .post("/api/orders")
          .then()
          .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
  }

  @Nested
  class GetOrdersTests {

    @Test
    void shouldGetOrdersSuccessfully() {
      List<OrderSummary> orderSummaries =
          given()
              .when()
              .get("/api/orders")
              .then()
              .statusCode(200)
              .extract()
              .body()
              .as(new TypeRef<>() {});
      assertThat(orderSummaries).hasSize(2);
    }
  }

  @Nested
  class GetOrderByOrderNumberTests {
    String orderNumber = "order-123";

    @Test
    void shouldGetOrderSuccessfully() {
      given()
          .when()
          .get("/api/orders/{orderNumber}", orderNumber)
          .then()
          .statusCode(200)
          .body("orderNumber", is(orderNumber))
          .body("items.size()", is(2));
    }
  }
}
