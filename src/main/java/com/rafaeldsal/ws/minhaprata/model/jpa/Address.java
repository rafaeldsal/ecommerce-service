package com.rafaeldsal.ws.minhaprata.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

  @Id
  @Column(name = "address_id", unique = true, nullable = false, updatable = false)
  private String id;

  private String street;
  private String number;
  private String complement;
  private String neighborhood;
  private String city;
  private String state;

  @Column(name = "postal_code")
  private String postalCode;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true, nullable = false)
  private User user;
}
