package com.mddev.bookstore.catalog.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(
    properties = {
      "spring.test.database.replace=none",
      "spring.datasource.url=jdbc:tc:postgresql:16-alpine:///db",
    })
// @Import(TestcontainersConfiguration.class)
@Sql("/test-data.sql")
public class ProductRepositoryTest {

  @Autowired private ProductRepository productRepository;

  @Test
  void shouldGetAllProducts() {
    List<ProductEntity> products = productRepository.findAll();
    assertThat(products).hasSize(15);
  }

  @Test
  void shouldGetProductByCode() {
    ProductEntity product = productRepository.findByCode("B016").orElseThrow();
    assertThat(product.getCode()).isEqualTo("B016");
    assertThat(product.getName()).isEqualTo("The Alchemist");
    assertThat(product.getDescription())
        .isEqualTo("A novel by Paulo Coelho about a boy pursuing his dream.");
    assertThat(product.getImageUrl())
        .isEqualTo("https://covers.openlibrary.org/b/isbn/9780061122415-L.jpg");
    assertThat(product.getPrice()).isEqualTo(new BigDecimal("10.50"));
  }

  @Test
  void shouldReturnProductWhenProductCodeNotExists() {
    assertThat(productRepository.findByCode("7768")).isEmpty();
  }
}
