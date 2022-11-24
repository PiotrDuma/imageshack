package com.github.PiotrDuma.imageshack.api.registration;

public class EmailAuthenticationException extends RuntimeException{

  public EmailAuthenticationException() {
  }

  public EmailAuthenticationException(String message) {
    super(message);
  }
}
