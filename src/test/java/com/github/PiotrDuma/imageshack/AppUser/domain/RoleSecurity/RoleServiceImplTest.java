package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

  @Mock
  private AppRoleRepo repo;

  private RoleService roleService;

  @BeforeEach
  void setUp(){
    this.roleService = new RoleServiceImpl(repo);
  }

  @Test
  void findRoleReturnsNothingThenThrowException() {
    final AppRoleType roleType = AppRoleType.USER;
    String message = "ROLE "+ roleType+" NOT FOUND";
    //when
    Mockito.when(repo.findRoleByRoleType(roleType)).thenThrow(new NoSuchRoleException(message));

    //then
    Exception ex = assertThrows(NoSuchRoleException.class, () -> this.roleService.findRoleByRoleType(roleType));
    assertEquals(ex.getMessage(), message);
  }

  @Test
  void findRoleThenSuccessfullyReturnRoleTest() {
    final AppRoleType roleType = AppRoleType.USER;
    Role role = new Role(roleType, Set.of());
    //when
    Mockito.when(repo.findRoleByRoleType(roleType)).thenReturn(Optional.of(role));

    //then
    Role result = this.roleService.findRoleByRoleType(roleType);
    assertEquals(role, result);
    assertEquals(0, result.getAllowedOperations().size());
    assertEquals(roleType.getRole(), result.getAuthority());
  }
}