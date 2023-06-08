package com.github.PiotrDuma.imageshack.config.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@Configuration
@ActiveProfiles(value = "test")
public class BasicSecurityTestConfig extends AbstractSecurityConfig {

  /**
   * WebMvcTest requires mocks of beans injected into SpringSecurityConfig. Otherwise, it'll call
   * missing beans while loading ApplicationContext.
   *
   * source: <a href="https://stackoverflow.com/a/68031386">https://stackoverflow.com/a/68031386</a>
   */
  @MockBean
  private PasswordEncoder passwordEncoder;
  @MockBean
  private UserDetailsService userDetailsService;
}
