package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

public class RegistrationAuthException extends RuntimeException{
  public RegistrationAuthException() {
  }

  public RegistrationAuthException(String message) {
    super(message);
  }
}
