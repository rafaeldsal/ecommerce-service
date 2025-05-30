package com.rafaeldsal.ws.minhaprata.service.impl;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.category.CategoryMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.repository.jpa.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  @Override
  @Cacheable(value = "categoryCacheAll")
  public List<Category> readAll() {
    return categoryRepository.findAll();
  }

  @Override
  @Cacheable(value = "categoryCache", key = "#id")
  public Category findById(String id) {
    return getCategory(id);
  }

  @Override
  @CacheEvict(value = { "categoryCache", "categoryCacheAll" }, allEntries = true)
  public Category create(CategoryDto category) {

    if (Objects.nonNull(category.id())) {
      throw new BadRequestException("Id deve ser nulo");
    }

    return categoryRepository.save(CategoryMapper.fromDtoCategory(category));
  }

  @Override
  @CacheEvict(value = { "categoryCache", "categoryCacheAll" }, allEntries = true)
  public Category update(String id, CategoryDto category) {
    var categoryExisting = getCategory(id);
    CategoryMapper.updateEntityFromDto(category, categoryExisting);
    return categoryRepository.save(categoryExisting);
  }

  @Override
  @CacheEvict(value = { "categoryCache", "categoryCacheAll" }, allEntries = true)
  public void delete(String id) {
    getCategory(id);
    categoryRepository.deleteById(id);
  }

  private Category getCategory(String id) {
    Optional<Category> optionalCategory = categoryRepository.findById(id);

    if (optionalCategory.isEmpty()) {
      throw new NotFoundException("Category não encontrada");
    }

    return optionalCategory.get();
  }

}
