package com.rafaeldsal.ws.minhaprata.controller;

import com.rafaeldsal.ws.minhaprata.dto.category.CategoryDto;
import com.rafaeldsal.ws.minhaprata.model.jpa.Category;
import com.rafaeldsal.ws.minhaprata.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<Category>> readAll() {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.readAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Category> readAll(@PathVariable("id") String id) {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(id));
  }

  @PostMapping
  public ResponseEntity<Category> create(@Valid @RequestBody CategoryDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto));
  }

  @PutMapping("{id}")
  public ResponseEntity<Category> update(@PathVariable("id") String id, @RequestBody CategoryDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(id, dto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") String id) {
    categoryService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
