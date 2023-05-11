package com.github.PiotrDuma.imageshack.api.login;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {

  private CustomAuthenticationFailureHandler service;
  private HttpServletRequest request;
  private HttpServletResponse response;

  @BeforeEach
  void setUp(){
    this.service = new CustomAuthenticationFailureHandler();
    this.response = mock(HttpServletResponse.class);
    this.request = mock(HttpServletRequest.class);
  }

  @Test
  void checkResponseWhenDisabledExceptionIsProvided() throws IOException, ServletException {
    String url = "http://localhost:8080/login/error/inactive";
    DisabledException ex = new DisabledException("");

    this.service.onAuthenticationFailure(this.request, this.response, ex);

    verify(this.response, times(1)).sendRedirect(url);
    verify(this.response, times(1)).setStatus(HttpStatus.FORBIDDEN.value());
  }

  @Test
  void checkResponseWhenLockedExceptionIsProvided() throws IOException, ServletException {
    String url = "http://localhost:8080/login/error/suspended";
    LockedException ex = new LockedException("");

    this.service.onAuthenticationFailure(this.request, this.response, ex);

    verify(this.response, times(1)).sendRedirect(url);
    verify(this.response, times(1)).setStatus(HttpStatus.FORBIDDEN.value());
  }

  @Test
  void checkResponseWhenUsernameNotFoundExceptionIsProvided() throws IOException, ServletException {
    String url = "http://localhost:8080/login/error/login";
    UsernameNotFoundException ex = new UsernameNotFoundException("");

    this.service.onAuthenticationFailure(this.request, this.response, ex);

    verify(this.response, times(1)).sendRedirect(url);
    verify(this.response, times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void checkResponseWhenBadCredentialsExceptionIsProvided() throws IOException, ServletException {
    String url = "http://localhost:8080/login/error/password";
    BadCredentialsException ex = new BadCredentialsException("");

    this.service.onAuthenticationFailure(this.request, this.response, ex);

    verify(this.response, times(1)).sendRedirect(url);
    verify(this.response, times(1)).setStatus(HttpStatus.UNAUTHORIZED.value());
  }

  @Test
  void checkResponseWhenUnspecifiedExceptionIsProvided() throws IOException, ServletException {
    String url = "http://localhost:8080/login/error";
    AuthenticationException ex = mock(AuthenticationException.class);

    this.service.onAuthenticationFailure(this.request, this.response, ex);

    verify(this.response, times(1)).sendRedirect(url);
    verify(this.response, times(1)).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }
}