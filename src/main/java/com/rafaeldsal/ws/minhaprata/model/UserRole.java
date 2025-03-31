package com.rafaeldsal.ws.minhaprata.model;

public enum UserRole {

  ADMIN("admin"),
  USER("user"),
  GHOST("ghost");

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
}
