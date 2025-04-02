package com.rafaeldsal.ws.minhaprata.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rafaeldsal.ws.minhaprata.dto.CategoryDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.model.Category;
import com.rafaeldsal.ws.minhaprata.repository.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  CategoryServiceImpl(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Category> readAll() {
    return categoryRepository.findAll();
  }

  @Override
  public Category findById(Long id) {
    return getCategory(id);
  }

  @Override
  public Category create(CategoryDto category) {

    if (Objects.nonNull(category.id())) {
      throw new BadRequestException("Id deve ser nulo");
    }

    return categoryRepository.save(Category.builder()
        .id(category.id())
        .name(category.name())
        .description(category.description())
        .build());
  }

  @Override
  public Category update(Long id, CategoryDto category) {
    getCategory(id);

    return categoryRepository.save(Category.builder()
        .id(id)
        .name(category.name())
        .description(category.description())
        .build());
  }

  @Override
  public void delete(Long id) {
    getCategory(id);
    categoryRepository.deleteById(id);
  }

  private Category getCategory(Long id) {
    Optional<Category> optionalCategory = categoryRepository.findById(id);

    if (optionalCategory.isEmpty()) {
      throw new NotFoundException("Category não encontrada");
    }

    return optionalCategory.get();
  }

}
