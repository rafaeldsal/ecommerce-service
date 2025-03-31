package com.rafaeldsal.ws.minhaprata.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_category")
@Data
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private String description;

}
