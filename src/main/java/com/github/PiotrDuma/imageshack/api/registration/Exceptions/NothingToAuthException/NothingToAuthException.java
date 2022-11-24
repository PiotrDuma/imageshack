package com.github.PiotrDuma.imageshack.api.registration.Exceptions.NothingToAuthException;

public class NothingToAuthException extends RuntimeException{
  private static final String MESSAGE = "Nothing to authenticate";

  public NothingToAuthException() {
    super(MESSAGE);
  }
}
