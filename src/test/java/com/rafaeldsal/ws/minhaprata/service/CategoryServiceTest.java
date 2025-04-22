package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.repository.jpa.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.CategoryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private List<Category> mockCategories() {
    List<Category> categories = new ArrayList<>();
    Category category1 = new Category(1L, "Anéis", "xx");
    Category category2 = new Category(2L, "Colares", "xx");
    categories.add(category1);
    categories.add(category2);
    return categories;
  }

  @Test
  void given_findAll_when_thereAreCategoriesInDataBase_then_returnAllCategories() {
    List<Category> categories = mockCategories();
    Mockito.when(categoryRepository.findAll()).thenReturn(categories);

    var result = categoryService.readAll();

    Assertions.assertThat(result).isNotEmpty().hasSize(2);
  }
}
