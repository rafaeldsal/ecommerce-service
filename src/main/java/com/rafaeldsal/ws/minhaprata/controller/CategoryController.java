package com.rafaeldsal.ws.minhaprata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rafaeldsal.ws.minhaprata.model.Category;
import com.rafaeldsal.ws.minhaprata.repository.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryController {

  @Autowired
  private CategoryRepository categoryRepository;

  @GetMapping
  public ResponseEntity<List<Category>> readAll() {
    return ResponseEntity.status(HttpStatus.OK).body(categoryRepository.findAll());
  }
}
