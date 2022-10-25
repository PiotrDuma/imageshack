package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

public class PasswordResetTokenObject extends AbstractTokenObject {
  private static final TokenAuthType tokenAuthType= TokenAuthType.PASSWORD_RESET;
  private static final int activeTimeInMinutes = 30;

  public PasswordResetTokenObject(String email) {
    super(email, tokenAuthType, activeTimeInMinutes);
  }

  //TODO
}
