package com.rafaeldsal.ws.minhaprata.mapper;

import com.rafaeldsal.ws.minhaprata.dto.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;

public class CategoryMapper {

  public static Category fromDtoTCategory(CategoryDto dto) {
    return Category.builder()
        .id(dto.id())
        .name(dto.name())
        .description(dto.description())
        .build();
  }

  public static void updateEntityFromDto(CategoryDto categoryDto, Category category) {
    if (categoryDto.name() != null) {
      category.setName(categoryDto.name());
    }

    if (categoryDto.description() != null) {
      category.setDescription(categoryDto.description());
    }
  }
}
