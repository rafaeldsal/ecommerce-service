package com.rafaeldsal.ws.minhaprata.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true, updatable = false)
  private String email;

  @Column(nullable = false, unique = true, updatable = false)
  private String cpf;

  @Column(name = "phone_number", nullable = false)
  private String phoneNumber;

  @Column(name = "dt_birth", nullable = false)
  private LocalDate dtBirth;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "dt_created", updatable = false)
  private LocalDate dtCreated = LocalDate.now();

  @Column(name = "dt_updated", updatable = false)
  private LocalDate dtUpdated;
}
