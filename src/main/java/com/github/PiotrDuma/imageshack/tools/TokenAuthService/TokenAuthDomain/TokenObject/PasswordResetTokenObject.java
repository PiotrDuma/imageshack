package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

class PasswordResetTokenObject extends AbstractTokenObject {
  private static final TokenAuthType tokenAuthType= TokenAuthType.PASSWORD_RESET;
  private static final int activeTimeInMinutes = 30;

  protected PasswordResetTokenObject(String email, String tokenValue) {
    super(email, tokenValue, tokenAuthType, activeTimeInMinutes);
  }
}
