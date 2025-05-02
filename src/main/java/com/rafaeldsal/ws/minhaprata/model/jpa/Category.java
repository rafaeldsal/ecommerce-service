package com.rafaeldsal.ws.minhaprata.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_category")
public class Category implements Serializable {

  @Id
  @Column(name = "category_id", unique = true, nullable = false, updatable = false)
  private String id;

  @Column(nullable = false)
  private String name;

  private String description;

}
