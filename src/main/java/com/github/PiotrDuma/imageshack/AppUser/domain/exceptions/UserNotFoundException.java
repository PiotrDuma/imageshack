package com.github.PiotrDuma.imageshack.AppUser.domain.exceptions;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
  public UserNotFoundException() {
  }
}
