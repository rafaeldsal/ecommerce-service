package com.rafaeldsal.ws.minhaprata.mapper.category;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;

import java.util.UUID;

public class CategoryMapper {

  public static Category fromDtoCategory(CategoryDto dto) {
    return Category.builder()
        .id(dto.id() != null ? dto.id() : IdGenerator.UUIDGenerator("cat"))
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
