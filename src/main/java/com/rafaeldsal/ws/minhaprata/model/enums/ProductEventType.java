package com.rafaeldsal.ws.minhaprata.model.enums;

public enum ProductEventType {
  PRODUCT_CREATED("product_created"),
  PRODUCT_UPDATED("product_updated"),
  PRODUCT_DELETED("product_deleted"),
  PRODUCT_STOCK_UPDATED("product_stock_updated"),
  PRODUCT_PRICE_UPDATED("product_price_updated");

  private String eventType;

  ProductEventType(String eventType) {
    this.eventType = eventType;
  }

  public static ProductEventType fromString(String str) {
    for (ProductEventType eventType : ProductEventType.values()) {
      if (eventType.eventType.equalsIgnoreCase(str)) {
        return eventType;
      }
    }

    throw new IllegalArgumentException("Tipo do evento inválido: " + str);
  }
}
