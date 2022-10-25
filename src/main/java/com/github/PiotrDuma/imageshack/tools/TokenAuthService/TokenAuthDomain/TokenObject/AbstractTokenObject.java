package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

public abstract class AbstractTokenObject {
  private String tokenValue;
  private String email;
  private TokenAuthType tokenType;
  private int tokenActiveTimeMinutes;

  protected AbstractTokenObject(String email, TokenAuthType tokenAuthType, int activeTimeInMinutes) {
    //TODO
  }
}
