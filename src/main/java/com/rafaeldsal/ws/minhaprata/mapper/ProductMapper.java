package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.ProductDto;
import com.rafaeldsal.ws.minhaprata.model.Category;
import com.rafaeldsal.ws.minhaprata.model.Product;

public class ProductMapper {

  public static Product fromDtoToEntity(ProductDto dto, Category category) {
    return Product.builder()
        .id(dto.id())
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .imgUrl(dto.imgUrl())
        .category(category)
        .build();
  }
}
