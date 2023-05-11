package com.github.PiotrDuma.imageshack.api.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsAuthenticationProviderTest {

  private DaoAuthenticationProvider provider;
  private UserDetailsChecker checker;

  @BeforeEach
  void setUp() throws Exception{
    this.provider = new CustomUserDetailsAuthenticationProvider();
    Field field = provider.getClass().getDeclaredField("checker");
    field.setAccessible(true);
    this.checker = (UserDetailsChecker)field.get(provider);
  }

  @Test
  void checkShouldThrowDisabledExceptionWhenUserIsNotEnabled(){
    UserDetails user = mock(UserDetails.class);
    Authentication auth = mock(Authentication.class);

    when(user.isEnabled()).thenReturn(false);
    when(user.isAccountNonLocked()).thenReturn(true);

    Exception ex = assertThrows(DisabledException.class, () -> this.checker.check(user));
    assertEquals("User is disabled", ex.getMessage());
  }

  @Test
  void checkShouldThrowLockedExceptionWhenUserIsLocked(){
    UserDetails user = mock(UserDetails.class);
    Authentication auth = mock(Authentication.class);

    when(user.isAccountNonLocked()).thenReturn(false);

    Exception ex = assertThrows(LockedException.class, () -> this.checker.check(user));
    assertEquals("User account is locked", ex.getMessage());
  }
}