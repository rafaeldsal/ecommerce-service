package com.rafaeldsal.ws.minhaprata.model.jpa;

import com.rafaeldsal.ws.minhaprata.model.enums.UserRole;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "tbl_user_credentials")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_credentials_id", nullable = false, unique = true)
  private Long id;

  private String username;

  private String password;

  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(role);
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }
}
