package com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailAddressException;

//TODO: handler to redirect /login with email error.
public class RegistrationEmailAddressException extends RuntimeException{
  private static final String MESSAGE = "Account with that email doesn't exists.";
  public RegistrationEmailAddressException(){
    super(MESSAGE);
  }
}
