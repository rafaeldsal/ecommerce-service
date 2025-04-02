package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.Category;

public class CategoryMapper {

  public static Category fromDtoTCategory(CategoryDto dto) {
    return Category.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .build();
  }
}
