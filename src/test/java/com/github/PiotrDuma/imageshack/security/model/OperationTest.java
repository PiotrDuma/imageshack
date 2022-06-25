package com.github.PiotrDuma.imageshack.security.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class OperationTest {

  @Test
  void shouldReturnOperationAuthority() {
    AppOperationType authority = Arrays.stream(AppOperationType.values()).findAny().get();
    Operation operation = new Operation(authority);

    assertEquals(operation.getAuthority(), authority.getOperationPermission());
  }

  @Test
  void shouldReturnOperationEnumTypeName() {
    AppOperationType authority = Arrays.stream(AppOperationType.values()).findAny().get();
    Operation operation = new Operation(authority);

    assertEquals(operation.toString(), authority.name());
  }
}