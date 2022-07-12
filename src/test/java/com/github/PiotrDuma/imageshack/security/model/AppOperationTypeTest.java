package com.github.PiotrDuma.imageshack.security.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppOperationTypeTest {

  @Test
  void nameShouldReturnEnumType() {
    assertEquals(AppOperationType.CREATE.name(), "CREATE");
  }

  @Test
  void getOperationPermissionShouldReturnEnumValue() {
    assertEquals(AppOperationType.CREATE.getOperationPermission(), "OP_CREATE");
  }

  @Test
  void shouldReturnStreamOfEnum(){
    assertArrayEquals(AppOperationType.values(), AppOperationType.stream().toArray());
    assertEquals(AppOperationType.stream().count(), AppOperationType.values().length);
  }
}