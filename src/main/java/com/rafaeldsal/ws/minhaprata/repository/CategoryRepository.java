package com.rafaeldsal.ws.minhaprata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  
}
