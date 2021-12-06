package com.github.PiotrDuma.imageshack.security.model;

public enum AppRoleType {
  OWNER("ROLE_OWNER"),
  ADMIN("ROLE_ADMIN"),
  MODERATOR("ROLE_MODERATOR"),
  USER("ROLE_USER");

  private final String role;

  AppRoleType(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
