package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.NoSuchElementException;

public class NoSuchRoleException extends NoSuchElementException {

  public NoSuchRoleException() {
  }

  public NoSuchRoleException(String message) {
    super(message);
  }
}
