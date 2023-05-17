package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

/**
 * Root all exceptions related to Registration domain.
 */
public abstract class RegistrationException extends RuntimeException{

  /**
   * @param msg - detailed message
   * @param cause - stacktrace of cause
   */
  public RegistrationException(String msg, Throwable cause) {
    super(msg, cause);
  }

  /**
   * @param msg - detailed message
   */
  public RegistrationException(String msg) {
    super(msg);
  }
}
