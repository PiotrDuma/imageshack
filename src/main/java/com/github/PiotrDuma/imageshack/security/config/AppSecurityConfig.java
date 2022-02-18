package com.github.PiotrDuma.imageshack.security.config;

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

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
  private final static String LOGIN_URL = "/login";
  private final static String LOGOUT_URL = "/logout";
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
        .authorizeRequests()
        .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin();
//        .formLogin()
//          .loginPage(LOGIN_URL)
//          .defaultSuccessUrl("/", true)
//          .failureUrl("/login?error=true")
//          .permitAll()
//        .and()
//        .logout()
//          .logoutUrl(LOGOUT_URL)
//          .logoutSuccessUrl("/")
//          .deleteCookies("JSESSIONID", "JWT").invalidateHttpSession(true)
//          .permitAll();
  }


  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService);
  }

  @Bean
  public DaoAuthenticationProvider userDetailsServiceProvider(){
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService());
    return provider;
  }
}
