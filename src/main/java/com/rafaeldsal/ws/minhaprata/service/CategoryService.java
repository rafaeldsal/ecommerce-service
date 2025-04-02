package com.rafaeldsal.ws.minhaprata.service;

import java.util.List;

import com.rafaeldsal.ws.minhaprata.dto.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.Category;

public interface CategoryService {

  List<Category> readAll();

  Category findById(Long id);

  Category create(CategoryDto category);

  Category update(Long id, CategoryDto category);

  void delete(Long id);
}
