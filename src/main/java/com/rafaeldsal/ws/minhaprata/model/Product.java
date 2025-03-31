package com.rafaeldsal.ws.minhaprata.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categories_id")
  private Category category;

}
