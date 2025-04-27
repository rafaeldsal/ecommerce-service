package com.rafaeldsal.ws.minhaprata.repository.jpa;

import com.rafaeldsal.ws.minhaprata.model.jpa.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

  Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Product p WHERE p.id IN :ids")
  List<Product> findAllByForUpdate(@Param("ids") List<String> ids);
}
