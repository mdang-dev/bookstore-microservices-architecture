package com.mddev.bookstore.orders.web.exception;

import com.mddev.bookstore.orders.domain.InvalidOrderException;
import com.mddev.bookstore.orders.domain.OrderNotFoundException;
import io.github.resilience4j.core.lang.NonNull;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private static final URI NOT_FOUND_TYPE =
      URI.create("https://bookstore.api.com/errors/not-found");
  private static final URI ISE_FOUND_TYPE =
      URI.create("https://bookstore.api.com/errors/server-error");
  private static final URI BAD_REQUEST_TYPE =
      URI.create("https://bookstore.api.com/errors/bad-request");
  private static final String SERVICE_NAME = "order-service";

  @ExceptionHandler(Exception.class)
  ProblemDetail handleUnhandledException(Exception e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setType(ISE_FOUND_TYPE);
    problemDetail.setProperty("service", SERVICE_NAME);
    problemDetail.setProperty("error_category", "Generic");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(OrderNotFoundException.class)
  ProblemDetail handleProductNotFoundException(OrderNotFoundException e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    problemDetail.setTitle("Product Not Found");
    problemDetail.setType(NOT_FOUND_TYPE);
    problemDetail.setProperty("service", SERVICE_NAME);
    problemDetail.setProperty("error_category", "Generic");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(InvalidOrderException.class)
  ProblemDetail handleInvalidOrderException(InvalidOrderException e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    problemDetail.setTitle("Invalid Order Creation Request");
    problemDetail.setType(BAD_REQUEST_TYPE);
    problemDetail.setProperty("service", SERVICE_NAME);
    problemDetail.setProperty("error_category", "Generic");
    problemDetail.setProperty("timestamp", Instant.now());

    return problemDetail;
  }

  @Override
  @Nullable
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    List<String> errors = new ArrayList<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (err) -> {
              String errMessage = err.getDefaultMessage();
              errors.add(errMessage);
            });

    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request payload");
    problemDetail.setTitle("Bad Request");
    problemDetail.setType(BAD_REQUEST_TYPE);
    problemDetail.setProperty("errors", errors);
    problemDetail.setProperty("service", SERVICE_NAME);
    problemDetail.setProperty("error_category", "Generic");
    problemDetail.setProperty("timestamp", Instant.now());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
  }
}
