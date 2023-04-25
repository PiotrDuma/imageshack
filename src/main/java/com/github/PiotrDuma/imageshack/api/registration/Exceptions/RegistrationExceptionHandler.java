package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler {

  @ExceptionHandler(RegistrationAuthException.class)
  public String handleRegistrationAuthException(RegistrationAuthException ex){
    return "login"; //TODO: prepare custom login page.
  }

}
