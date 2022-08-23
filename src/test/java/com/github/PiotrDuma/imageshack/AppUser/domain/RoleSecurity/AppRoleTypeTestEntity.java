package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import org.junit.jupiter.api.Test;

class AppRoleTypeTestEntity {

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