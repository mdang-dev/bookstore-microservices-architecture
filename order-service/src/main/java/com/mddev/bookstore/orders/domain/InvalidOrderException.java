package com.mddev.bookstore.orders.domain;

public class InvalidOrderException extends RuntimeException {

  public InvalidOrderException(String messages) {
    super(messages);
  }
}
