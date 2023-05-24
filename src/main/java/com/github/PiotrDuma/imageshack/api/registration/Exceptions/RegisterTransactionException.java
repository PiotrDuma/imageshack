package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

public class RegisterTransactionException extends RegistrationException{

  public RegisterTransactionException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public RegisterTransactionException(String msg) {
    super(msg);
  }
}
