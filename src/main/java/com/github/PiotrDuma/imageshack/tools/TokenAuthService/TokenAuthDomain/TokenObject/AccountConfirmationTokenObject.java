package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

class AccountConfirmationTokenObject extends AbstractTokenObject {
  private static final TokenAuthType tokenAuthType= TokenAuthType.ACCOUNT_CONFIRMATION;
  private static final int activeTimeInMinutes = 60;

  protected AccountConfirmationTokenObject(String email, String tokenValue) {
    super(email, tokenValue, tokenAuthType, activeTimeInMinutes);
  }
}
