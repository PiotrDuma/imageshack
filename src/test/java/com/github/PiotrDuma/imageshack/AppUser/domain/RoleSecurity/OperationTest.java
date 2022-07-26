package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppOperationType;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Operation;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

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