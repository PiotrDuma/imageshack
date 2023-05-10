package com.github.PiotrDuma.imageshack.security.config;

import com.github.PiotrDuma.imageshack.api.login.CustomAuthenticationFailureHandler;
import com.github.PiotrDuma.imageshack.api.login.CustomUserDetailsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AppSecurityConfig extends WebSecurityConfigurerAdapter {

  private final static String LOGIN_URL = "/login";
  private final static String LOGOUT_URL = "/logout";
  private final static String REGISTRATION_URL = "/register/**";
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  @Autowired
  public AppSecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .headers().frameOptions().disable().and() //required for H2 database.
        .authorizeRequests()
        .antMatchers("/", "/index", "/static/**", "/js/**", "/css/**",
                        "/img/**", "/json/**").permitAll()
        .antMatchers(LOGIN_URL+"/**").permitAll()
        .antMatchers(REGISTRATION_URL, "/api/securitytest/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
          .loginPage(LOGIN_URL)
          .defaultSuccessUrl("/api/securitytest/info", true)
          .failureUrl("/login/error")
          .failureHandler(this.customAuthFailureHandler())
          .permitAll()
        .and()
        .logout()
          .logoutUrl(LOGOUT_URL)
          .logoutSuccessUrl("/")
          .clearAuthentication(true)
          .invalidateHttpSession(true)
          .deleteCookies("JSESSIONID", "JWT").invalidateHttpSession(true)
          .permitAll();
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(userDetailsServiceProvider());
  }

  @Bean
  public AuthenticationFailureHandler customAuthFailureHandler(){
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public DaoAuthenticationProvider userDetailsServiceProvider() {
//    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    DaoAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    provider.setHideUserNotFoundExceptions(false);
    return provider;
  }
}
