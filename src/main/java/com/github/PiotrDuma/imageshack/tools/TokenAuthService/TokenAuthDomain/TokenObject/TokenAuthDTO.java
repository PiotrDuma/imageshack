package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

public class TokenAuthDTO {
  private String email;
  private TokenAuthType tokenType;

  public TokenAuthDTO(String email, TokenAuthType tokenType) {
    this.email = email;
    this.tokenType = tokenType;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public TokenAuthType getTokenType() {
    return tokenType;
  }

  public void setTokenType(TokenAuthType tokenType) {
    this.tokenType = tokenType;
  }
}
