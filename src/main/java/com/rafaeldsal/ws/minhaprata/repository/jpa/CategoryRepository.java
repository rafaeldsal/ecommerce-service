package com.rafaeldsal.ws.minhaprata.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rafaeldsal.ws.minhaprata.model.jpa.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  
}
