package com.github.PiotrDuma.imageshack.api.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  /**
   * Method handles exceptions and forwards redirect to custom endpoint based on the exception class.
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    if(exception instanceof UsernameNotFoundException){
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.sendRedirect("http://localhost:8080/login/error/login");
    } else if(exception instanceof BadCredentialsException){
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.sendRedirect("http://localhost:8080/login/error/password");
    }else if(exception instanceof LockedException){
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.sendRedirect("http://localhost:8080/login/error/suspended");
    }else if(exception instanceof DisabledException){
      response.setStatus(HttpStatus.FORBIDDEN.value());
      response.sendRedirect("http://localhost:8080/login/error/inactive");
    }else if(exception != null){
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.sendRedirect("http://localhost:8080/login/error");
    }
  }
}