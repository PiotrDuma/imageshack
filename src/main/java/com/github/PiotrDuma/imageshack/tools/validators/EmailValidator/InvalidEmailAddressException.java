package com.github.PiotrDuma.imageshack.tools.validators.EmailValidator;

public class InvalidEmailAddressException extends RuntimeException{
  private static final String MESSAGE = "Invalid email address: %s.";

  public InvalidEmailAddressException(String email) {
    super(String.format(MESSAGE, email));
  }
}
