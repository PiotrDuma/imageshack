package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

class PasswordResetTokenObject extends AbstractTokenObject {
  private static final TokenAuthType TOKEN_AUTH_TYPE= TokenAuthType.PASSWORD_RESET;
  private static final int ACTIVE_TIME_IN_MINUTES = 30;

  protected PasswordResetTokenObject(String email, String tokenValue) {
    super(email, tokenValue, TOKEN_AUTH_TYPE, ACTIVE_TIME_IN_MINUTES);
  }
}
