package com.rafaeldsal.ws.minhaprata.mapper.product;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductCreatedEventDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductDataDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductRequestDto;
import com.rafaeldsal.ws.minhaprata.dto.product.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.model.enums.ProductEventType;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import com.rafaeldsal.ws.minhaprata.utils.DateTimeUtils;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;

public class ProductMapper {

  public static Product fromDtoToEntity(ProductRequestDto dto, Category category) {
    return Product.builder()
        .id(IdGenerator.UUIDGenerator("prod"))
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .imgUrl(dto.imgUrl())
        .stockQuantity(dto.stockQuantity())
        .currency(dto.currency())
        .dtCreated(dto.dtCreated() != null ? dto.dtCreated() : DateTimeUtils.now())
        .dtUpdated(dto.dtUpdated() != null ? dto.dtUpdated() : DateTimeUtils.now())
        .category(category)
        .build();
  }

  public static void updateEntityFromDto(ProductRequestDto dto, Product product, Category category) {

    if (dto == null || product == null) return;

    product.setName(dto.name() != null ? dto.name() : product.getName());
    product.setDescription(dto.description() != null ? dto.description() : product.getDescription());
    product.setPrice(dto.price() != null ? dto.price() : product.getPrice());
    product.setStockQuantity(dto.stockQuantity() != null ? dto.stockQuantity() : product.getStockQuantity());
    product.setImgUrl(dto.imgUrl() != null ? dto.imgUrl() : product.getImgUrl());
    product.setDtUpdated(DateTimeUtils.now());
    if (category != null) product.setCategory(category);
  }

  public static ProductResponseDto fromEntityToResponseDto(Product product) {
    return ProductResponseDto.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .imgUrl(product.getImgUrl())
        .stockQuantity(product.getStockQuantity())
        .dtCreated(product.getDtCreated())
        .dtUpdated(product.getDtUpdated())
        .category(product.getCategory() != null ?
            CategoryDto.builder()
                .id(product.getCategory().getId())
                .name(product.getCategory().getName())
                .description(product.getCategory().getDescription())
                .build() : null)
        .build();
  }

  public static ProductCreatedEventDto fromEntityToEventDto(Product product, ProductEventType eventType) {
    if (product == null) return null;

    return ProductCreatedEventDto.builder()
        .eventType(eventType)
        .timestamp(DateTimeUtils.timestamp())
        .data(ProductDataDto.builder()
            .productId(product.getId())
            .name(product.getName())
            .stockQuantity(product.getStockQuantity())
            .price(product.getPrice())
            .description(product.getDescription())
            .imageUrl(product.getImgUrl())
            .currency(product.getCurrency())
            .build())
        .build();
  }
}
