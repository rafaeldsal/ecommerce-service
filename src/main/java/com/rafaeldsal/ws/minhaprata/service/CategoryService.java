package com.rafaeldsal.ws.minhaprata.service;

import java.util.List;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;

public interface CategoryService {

  List<Category> readAll();

  Category findById(String id);

  Category create(CategoryDto category);

  Category update(String id, CategoryDto category);

  void delete(String id);
}
