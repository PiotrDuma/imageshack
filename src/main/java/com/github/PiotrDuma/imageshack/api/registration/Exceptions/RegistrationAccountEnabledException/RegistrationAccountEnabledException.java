package com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAccountEnabledException;

//TODO: handler redirect to login.
public class RegistrationAccountEnabledException extends RuntimeException{
  private static final String MESSAGE = "Account has been authenticated.";
  public RegistrationAccountEnabledException() {
    super(MESSAGE);
  }
}
