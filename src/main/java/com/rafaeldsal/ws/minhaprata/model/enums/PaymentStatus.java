package com.rafaeldsal.ws.minhaprata.model.enums;

public enum PaymentStatus {
  REQUIRES_PAYMENT_METHOD("requires_payment_method"),
  REQUIRES_CONFIRMATION("requires_confirmation"),
  REQUIRES_ACTION("requires_action"),
  PROCESSING("processing"),
  SUCCEEDED("succeeded"),
  CANCELED("canceled"),
  FAILED("failed");

  private final String status;

  PaymentStatus(String status) {
    this.status = status;
  }

  public static PaymentStatus fromString(String str) {
    for (PaymentStatus status : PaymentStatus.values()) {
      if (status.status.equalsIgnoreCase(str)) {
        return status;
      }
    }

    throw new IllegalArgumentException("Status inválido de pagamento: " + str);
  }

  public boolean isFinalStatus() {
    return this == SUCCEEDED || this == FAILED || this == CANCELED;
  }

  public boolean isFailureStatus() {
    return this == REQUIRES_PAYMENT_METHOD || this == FAILED || this == CANCELED;
  }

  public boolean isInProgress() {
    return this == REQUIRES_ACTION || this == REQUIRES_CONFIRMATION || this == PROCESSING;
  }
}
