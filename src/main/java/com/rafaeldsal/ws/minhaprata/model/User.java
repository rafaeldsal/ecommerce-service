package com.rafaeldsal.ws.minhaprata.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_users")
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, updatable = false)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true, updatable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(updatable = false)
  private LocalDate createdAt = LocalDate.now();

  @Column(updatable = false)
  private LocalDate updatedAt;
}
