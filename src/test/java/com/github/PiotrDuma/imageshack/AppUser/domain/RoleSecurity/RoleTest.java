package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Operation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleTest {

  @Mock
  Operation operation;
  private Role role;
  private AppRoleType roleType;
  private Set<Operation> operations;

  @BeforeEach
  void setUp(){
    this.roleType = Arrays.stream(AppRoleType.values()).findAny().get();
    this.operations = new HashSet<>();
    operations.add(operation);
    this.role = new Role(roleType, operations);
  }

  @Test
  void getAuthorityShouldReturnRoleValue() {
      assertEquals(role.getAuthority(), roleType.getRole());
    }

  @Test
  void getAllowedOperationsShouldReturnSetOfOperations() {
    assertEquals(role.getAllowedOperations(),this.operations);
    assertEquals(role.getAllowedOperations().size(), 1);
  }

  @Test
  void setAllowedOperations() {
    //given
    Set<Operation> newSet = new HashSet<>();
    newSet.add(mock(Operation.class));
    newSet.add(mock(Operation.class));

    //when
    role.setAllowedOperations(newSet);

    //then
    assertEquals(role.getAllowedOperations(), newSet);
    assertEquals(role.getAllowedOperations().size(), 2);
  }

  @Test
  void toStringShouldReturnEnumType() {
    assertEquals(this.role.toString(), this.roleType.name());
  }
}