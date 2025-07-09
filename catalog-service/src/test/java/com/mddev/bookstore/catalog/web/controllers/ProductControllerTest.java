package com.mddev.bookstore.catalog.web.controllers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import com.mddev.bookstore.catalog.AbstractIT;
import com.mddev.bookstore.catalog.domain.Product;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql("/test-data.sql")
public class ProductControllerTest extends AbstractIT {
  @Test
  void shouldReturnProducts() {
    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/products")
        .then()
        .statusCode(200)
        .body("data", hasSize(10))
        .body("totalElements", is(15))
        .body("pageNumber", is(1))
        .body("totalPages", is(2))
        .body("isFirst", is(true))
        .body("isLast", is(false))
        .body("hasNext", is(true))
        .body("hasPrevious", is(false));
  }

  @Test
  void shouldGetProductByCode() {
    Product product =
        given()
            .contentType(ContentType.JSON)
            .when()
            .get("/api/products/{code}", "B016")
            .then()
            .statusCode(200)
            .assertThat()
            .extract()
            .body()
            .as(Product.class);
    assertThat(product.code()).isEqualTo("B016");
    assertThat(product.name()).isEqualTo("The Alchemist");
    assertThat(product.description())
        .isEqualTo("A novel by Paulo Coelho about a boy pursuing his dream.");
    assertThat(product.imageUrl())
        .isEqualTo("https://covers.openlibrary.org/b/isbn/9780061122415-L.jpg");
    assertThat(product.price()).isEqualTo(new BigDecimal("10.50"));
  }

  @Test
  void shouldReturnProductWhenProductCodeNotExists() {
    String code = "invalid_code";
    given()
        .contentType(ContentType.JSON)
        .when()
        .get("/api/products/{code}", code)
        .then()
        .statusCode(404)
        .body("status", is(404))
        .body("title", is("Product Not Found"))
        .body("detail", is("Product with code " + code + " not found"));
  }
}
