package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.CategoryDto;
import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.dto.ProductResponseDto;
import com.rafaeldsal.ws.minhaprata.model.Category;
import com.rafaeldsal.ws.minhaprata.model.Product;

import java.time.LocalDateTime;

public class ProductMapper {

  public static Product fromDtoToEntity(ProductDto dto, Category category) {
    return Product.builder()
        .id(dto.id())
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .imgUrl(dto.imgUrl())
        .stockQuantity(dto.stockQuantity())
        .dtCreated(dto.dtCreated() != null ? dto.dtCreated() : LocalDateTime.now())
        .dtUpdated(dto.dtUpdated() != null ? dto.dtUpdated() : LocalDateTime.now())
        .category(category)
        .build();
  }

  public static void updateEntityFromDto(ProductDto dto, Product product, Category category) {

    if (dto == null || product == null) return;

    product.setName(dto.name() != null ? dto.name() : product.getName());
    product.setDescription(dto.description() != null ? dto.description() : product.getDescription());
    product.setPrice(dto.price() != null ? dto.price() : product.getPrice());
    product.setStockQuantity(dto.stockQuantity() != null ? dto.stockQuantity() : product.getStockQuantity());
    product.setImgUrl(dto.imgUrl() != null ? dto.imgUrl() : product.getImgUrl());
    product.setDtUpdated(LocalDateTime.now());
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
}
