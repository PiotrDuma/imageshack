package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

class AccountConfirmationTokenObject extends AbstractTokenObject {
  private static final TokenAuthType TOKEN_AUTH_TYPE= TokenAuthType.ACCOUNT_CONFIRMATION;
  private static final int ACTIVE_TIME_IN_MINUTES = 60;

  protected AccountConfirmationTokenObject(String email, String tokenValue) {
    super(email, tokenValue, TOKEN_AUTH_TYPE, ACTIVE_TIME_IN_MINUTES);
  }
}
