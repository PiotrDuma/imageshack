package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

/**
 * Exception thrown when method affects invalid input data. e.g. account is not found, not exist
 * or invoking method is unnecessary(account already activated).
 */
public class RegistrationAuthAccountException extends RegistrationException{

  public RegistrationAuthAccountException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public RegistrationAuthAccountException(String msg) {
    super(msg);
  }
}
