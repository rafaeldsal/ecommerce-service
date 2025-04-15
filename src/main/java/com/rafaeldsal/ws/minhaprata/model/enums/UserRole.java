package com.rafaeldsal.ws.minhaprata.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

  ADMIN("admin"),
  USER("user");

  private String role;

  UserRole(String role) {
    this.role = role;
  }

  public static UserRole fromString(String str) {
    for (UserRole role : UserRole.values()) {
      if (role.role.equalsIgnoreCase(str)) {
        return role;
      }
    }

    throw new IllegalArgumentException("Role inválida: " + str);
  }

  @Override
  public String getAuthority() {
    return "ROLE_" + role.toUpperCase();
  }
}
