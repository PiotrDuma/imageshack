package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

/**
 * Throws during internal service authentication processing exceptions, e.g. token validation, data transaction.
 */
public class RegistrationAuthException extends RegistrationException{

  public RegistrationAuthException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public RegistrationAuthException(String msg) {
    super(msg);
  }
}
