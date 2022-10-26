package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

abstract class AbstractTokenObject implements TokenObject{
  private final String tokenValue;
  private final String email;
  private TokenAuthType tokenType;
  private int tokenActiveTimeMinutes;

  protected AbstractTokenObject(String email, String tokenValue, TokenAuthType tokenAuthType,
      int activeTimeInMinutes) {
    this.tokenValue = tokenValue;
    this.email = email;
    //TODO
  }

  public String getTokenValue() {
    return tokenValue;
  }

  public String getEmail() {
    return email;
  }

  public TokenAuthType getTokenType() {
    return tokenType;
  }

  public void setTokenType(
      TokenAuthType tokenType) {
    this.tokenType = tokenType;
  }

  public int getTokenActiveTimeMinutes() {
    return tokenActiveTimeMinutes;
  }

  public void setTokenActiveTimeMinutes(int tokenActiveTimeMinutes) {
    this.tokenActiveTimeMinutes = tokenActiveTimeMinutes;
  }
}
