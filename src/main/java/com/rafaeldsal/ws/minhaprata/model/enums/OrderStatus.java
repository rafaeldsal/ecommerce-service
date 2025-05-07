package com.rafaeldsal.ws.minhaprata.model.enums;

public enum OrderStatus {
  PENDING("pending"),
  PENDING_PAYMENT("pending_payment"),
  PAID("paid"),
  IN_PROCESSING("in_processing"),
  SHIPPED("shipped"),
  DELIVERED("delivered"),
  CANCELLED("canceled"),
  EXPIRED("expired"),
  FAILURE("failure");

  private String status;

  OrderStatus(String status) {
    this.status = status;
  }

  public static OrderStatus fromString(String str) {
    for (OrderStatus status : OrderStatus.values()) {
      if (status.status.equalsIgnoreCase(str)) {
        return status;
      }
    }

    throw new IllegalArgumentException("Status inválido: " + str);
  }

  public boolean isFinalStatus() {
    return this == CANCELLED || this == DELIVERED || this == FAILURE;
  }
}
