package com.mddev.bookstore.orders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AbstarctIT {
  @LocalServerPort int port;

  static WireMockContainer wireMockServer = new WireMockContainer("wiremock/wiremock:3.5.2-alpine");

  @BeforeAll
  static void beforeAll() {
    wireMockServer.start();
    configureFor(wireMockServer.getHost(), wireMockServer.getPort());
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("orders.catalog-service-url", wireMockServer::getBaseUrl);
  }

  @BeforeEach
  void setUp() {
    RestAssured.port = port;
  }

  protected static void mockGetProductByCode(String code, String name, BigDecimal price) {
    stubFor(
        WireMock.get(urlMatching("/api/products/" + code))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withStatus(200)
                    .withBody(
                        """
                            {
                              "code": "%s",
                              "name": "%s",
                              "price": "%f"
                            }
                                        """
                            .formatted(code, name, price.doubleValue()))));
  }
}
