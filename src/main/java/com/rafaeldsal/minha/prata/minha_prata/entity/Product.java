package com.rafaeldsal.minha.prata.minha_prata.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "tbl_products")
@Data
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, unique = true, updatable = false)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  private String imgUrl;

  @OneToMany(mappedBy = "product")
  private List<OrderItem> orderItems;

}
