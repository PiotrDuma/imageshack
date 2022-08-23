package com.github.PiotrDuma.imageshack.AppUser.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Operation;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Role;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

@ExtendWith(MockitoExtension.class)
class UserDetailsWrapperTest {

  @Test
  void builderPatternTest() {
    boolean isEnabled = false;
    boolean isAccountNonExpired = false;
    boolean isAccountNonLocked = false;
    boolean isCredentialsNonExpired = false;
    String email = "test@email.com";
    String password = "passwd";
    String username = "usrname";

    UserDetailsWrapper result = new UserDetailsWrapper.Builder(username, email, password)
        .accountNonExpired(isAccountNonExpired)
        .accountNonLocked(isAccountNonLocked)
        .enabled(isEnabled)
        .credentialsNonExpired(isCredentialsNonExpired)
        .build();

    assertEquals(isEnabled, result.getCustomUserDetails().isEnabled());
    assertEquals(isAccountNonExpired, result.getCustomUserDetails().isAccountNonExpired());
    assertEquals(isAccountNonLocked, result.getCustomUserDetails().isAccountNonLocked());
    assertEquals(isCredentialsNonExpired, result.getCustomUserDetails().isCredentialsNonExpired());
    assertEquals(email, result.getEmail());
    assertEquals(username, result.getUsername());
    assertEquals(password, result.getPassword());
  }

  @Test
  void wrapperAuthoritiesCollectionTest(){
    Operation op1 = Mockito.mock(Operation.class);
    Operation op2 = Mockito.mock(Operation.class);
    Operation op3 = Mockito.mock(Operation.class);
    Role role1 = Mockito.mock(Role.class);
    Role role2 = Mockito.mock(Role.class);


    Set<GrantedAuthority> role1Set = Set.of(op1, op2);
    Mockito.doReturn(role1Set).when(role1).getAllowedOperations();

    Set<GrantedAuthority> role2Set = Set.of(op3);
    Mockito.doReturn(role2Set).when(role2).getAllowedOperations();

    Set<Role> roles = Set.of(role1, role2);
    UserDetailsWrapper wrapper = new UserDetailsWrapper(Mockito.mock(User.class));
    Mockito.when(wrapper.getUser().getRoles()).thenReturn(roles);

    Collection<? extends GrantedAuthority> results = wrapper.getAuthorities();

    assertEquals(5, results.size());
    assertTrue(results.contains(op1));
    assertTrue(results.contains(op3));
    assertTrue(results.contains(role1));
    assertTrue(results.contains(role2));
    assertTrue(results.contains(op2));
  }

}