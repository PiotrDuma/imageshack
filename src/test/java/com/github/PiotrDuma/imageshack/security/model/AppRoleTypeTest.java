package com.github.PiotrDuma.imageshack.security.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppRoleTypeTest {

  @Test
  void nameShouldReturnEnumType() {
    assertEquals(AppRoleType.OWNER.name(), "OWNER");
  }

  @Test
  void getRoleShouldReturnEnumValue() {
    assertEquals(AppRoleType.OWNER.getRole(), "ROLE_OWNER");
  }

  @Test
  void checkCorrectEnumTypeValues(){
    assertAll(
        () -> assertEquals(AppRoleType.OWNER.getRole(), "ROLE_OWNER"),
        () -> assertEquals(AppRoleType.ADMIN.getRole(), "ROLE_ADMIN"),
        () -> assertEquals(AppRoleType.MODERATOR.getRole(), "ROLE_MODERATOR"),
        () -> assertEquals(AppRoleType.USER.getRole(), "ROLE_USER")
    );
  }
}