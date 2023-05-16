package com.github.PiotrDuma.imageshack.api.login;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsAuthenticationProviderTest {
  //invoke by field
  private UserDetailsChecker preChecker;
  private UserDetailsChecker postChecker;

  @BeforeEach
  void setUp() throws Exception{
    DaoAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider();

    Field fieldPreChecker = provider.getClass().getDeclaredField("preChecker");
    fieldPreChecker.setAccessible(true);
    this.preChecker = (UserDetailsChecker)fieldPreChecker.get(provider);

    Field fieldPostChecker = provider.getClass().getDeclaredField("postChecker");
    fieldPostChecker.setAccessible(true);
    this.postChecker = (UserDetailsChecker)fieldPostChecker.get(provider);
  }

  @Test
  void postCheckShouldThrowDisabledExceptionWhenUserIsNotEnabled(){
    UserDetails user = mock(UserDetails.class);

    when(user.isEnabled()).thenReturn(false);
    when(user.isAccountNonLocked()).thenReturn(true);

    Exception ex = assertThrows(DisabledException.class, () -> this.postChecker.check(user));
    assertEquals("User is disabled", ex.getMessage());
  }

  @Test
  void postCheckShouldThrowLockedExceptionWhenUserIsLocked(){
    UserDetails user = mock(UserDetails.class);

    when(user.isAccountNonLocked()).thenReturn(false);

    Exception ex = assertThrows(LockedException.class, () -> this.postChecker.check(user));
    assertEquals("User account is locked", ex.getMessage());
  }

  @Test
  void preCheckShouldDoNothing(){
    UserDetails user = mock(UserDetails.class);
    UserDetailsChecker spy = Mockito.spy(preChecker);

    spy.check(user);
    verify(spy, times(1)).check(user);
  }
}