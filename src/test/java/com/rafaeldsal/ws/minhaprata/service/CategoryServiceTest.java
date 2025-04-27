package com.rafaeldsal.ws.minhaprata.service;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.exception.BadRequestException;
import com.rafaeldsal.ws.minhaprata.exception.NotFoundException;
import com.rafaeldsal.ws.minhaprata.mapper.category.CategoryMapper;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.repository.jpa.CategoryRepository;
import com.rafaeldsal.ws.minhaprata.service.impl.CategoryServiceImpl;
import com.rafaeldsal.ws.minhaprata.utils.IdGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

  private static final String ID = IdGenerator.UUIDGenerator("cat");

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private List<Category> mockCategories() {

    return List.of(
        new Category(IdGenerator.UUIDGenerator("cat"), "Anéis", "xx"),
        new Category(IdGenerator.UUIDGenerator("cat"), "Colares", "xx")
    );
  }

  @Test
  void given_findAll_when_thereAreCategoriesInDataBase_then_returnAllCategories() {
    List<Category> categories = mockCategories();
    when(categoryRepository.findAll()).thenReturn(categories);

    var result = categoryService.readAll();

    Assertions.assertFalse(result.isEmpty());
    Assertions.assertEquals(2, result.size());
  }

  @Test
  void given_findAll_when_thereAreNotCategoriesInDataBase_then_returnEmptyList() {
    when(categoryRepository.findAll()).thenReturn(List.of());
    var result = categoryService.readAll();
    Assertions.assertTrue(result.isEmpty());

    verify(categoryRepository, times(1)).findAll();
  }

  @Test
  void given_findById_when_thereAreCategoryInDataBaseById_then_returnCategory() {
    Category category = new Category(ID, "Anéis", "xx");
    when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
    Assertions.assertEquals(category, categoryService.findById(ID));

    verify(categoryRepository, times(1)).findById(ID);
  }

  @Test
  void given_findById_when_categoryRepositoryReturnEmptyOptional_then_return_throwNotFoundException() {
    when(categoryRepository.findById(ID)).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class, () -> categoryService.findById(ID));

    verify(categoryRepository, times(1)).findById(ID);
  }

  @Test
  void given_create_when_categoryIdIsNull_then_returnCreatedCategory() {
   var dtoWithoutId = CategoryDto.builder()
       .name("Bijuteria")
       .description("Categoria teste")
       .build();

   when(categoryRepository.save(any(Category.class)))
       .thenAnswer(invocation -> invocation.getArgument(0));

   Category category = categoryService.create(dtoWithoutId);

   // Asserts
    Assertions.assertEquals(dtoWithoutId.name(), category.getName());
    Assertions.assertEquals(dtoWithoutId.description(), category.getDescription());

    verify(categoryRepository, times(1)).save(category);
  }

  @Test
  void given_create_when_categoryIdIsNotNull_then_return_throwBadRequestException() {
    var dtoWithId = CategoryDto.builder()
        .id(ID)
        .name("Bijuteria")
        .description("Categoria teste")
        .build();

    Assertions.assertThrows(BadRequestException.class, () -> categoryService.create(dtoWithId));

    verify(categoryRepository, never()).save(any());
  }

  @Test
  void given_update_when_thereAreCategoryDtoAndCategoryId_then_returnUpdatedCategory() {
    var dtoWithoutId = CategoryDto.builder()
        .name("Bijuteria")
        .description("Categoria teste")
        .build();

    Category category = new Category(ID, "Anéis", "xx");
    when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
    when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Category result = categoryService.update(ID, dtoWithoutId);

    Assertions.assertEquals(dtoWithoutId.name(), result.getName());
    Assertions.assertEquals(dtoWithoutId.description(), result.getDescription());

    verify(categoryRepository, times(1)).findById(ID);
    verify(categoryRepository, times(1)).save(category);
  }

  @Test
  void given_update_when_categoryDoesNotExist_then_throwNotFoundException() {
    var dtoWithoutId = CategoryDto.builder()
        .id(ID)
        .name("Bijuteria")
        .description("Categoria teste")
        .build();

    when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> categoryService.update(ID, dtoWithoutId));

    verify(categoryRepository, never()).save(any());
  }

  @Test
  void given_categoryDto_when_updateEntityFromDto_then_updateFieldsInCategory() {
    CategoryDto dto = CategoryDto.builder()
        .name("Bijuteria")
        .description("Categoria de teste")
        .build();

    Category category = new Category(ID, "Antigo nome", "Antiga descrição");

    CategoryMapper.updateEntityFromDto(dto, category);

    Assertions.assertEquals(dto.name(), category.getName());
    Assertions.assertEquals(dto.description(), category.getDescription());
  }

  @Test
  void given_delete_when_CategoryExistsInDataBase_then_returnDeletedCategory() {
    Category category = new Category(ID, "Anéis", "xx");
    when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));

    categoryService.delete(ID);

    verify(categoryRepository, times(1)).deleteById(ID);
  }

  @Test
  void given_delete_when_categoryDoesNotExist_then_throwNotFoundException() {
    when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> categoryService.delete(ID));

    verify(categoryRepository, never()).deleteById(ID);
  }
}
