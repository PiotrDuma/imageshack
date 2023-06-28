package com.github.PiotrDuma.imageshack.api.passwordreset;

import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;

public interface PasswordResetService {

  void authenticate(String email, String token) throws BadRequestException; //TODO: exception
  void reset(String email, String password) throws PasswordResetException;
}
