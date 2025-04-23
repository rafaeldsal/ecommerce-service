package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.CategoryDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.CategoryMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.repository.jpa.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    return categoryRepository.save(CategoryMapper.fromDtoTCategory(category));
  }

  @Override
  public Category update(Long id, CategoryDto category) {
    var categoryExisting = getCategory(id);
    CategoryMapper.updateEntityFromDto(category, categoryExisting);
    return categoryRepository.save(categoryExisting);
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
