package com.github.PiotrDuma.imageshack.api.passwordreset;

import org.springframework.stereotype.Service;

@Service("passwordResetService")
public class PasswordResetServiceImpl implements PasswordResetService {

  @Override
  public void authenticate(String email, String token) throws RuntimeException {
    //TODO
  }
}
