package com.github.PiotrDuma.imageshack.api.passwordreset;

import com.github.PiotrDuma.imageshack.exception.type.BadRequestException;
import org.springframework.stereotype.Service;

@Service("passwordResetService")
class PasswordResetServiceImpl implements PasswordResetService {

  @Override
  public void authenticate(String email, String token) throws BadRequestException {
    //TODO
  }

  @Override
  public void reset(String email, String password) throws PasswordResetException {

  }
}
