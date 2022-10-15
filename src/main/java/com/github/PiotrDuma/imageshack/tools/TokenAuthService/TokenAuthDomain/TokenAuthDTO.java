package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuth;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import java.time.LocalDateTime;

public class TokenAuthDTO {
  private String email;
  private String token;
  private TokenAuthType tokenType;
  private LocalDateTime created;
  private LocalDateTime expired;

  public TokenAuthDTO(String email, String token, TokenAuthType tokenType, LocalDateTime created,
      LocalDateTime expired) {
    this.email = email;
    this.token = token;
    this.tokenType = tokenType;
    this.created = created;
    this.expired = expired;
  }

  protected TokenAuthDTO(TokenAuth tokenAuth) {
    this.email = tokenAuth.getEmail();
    this.token = tokenAuth.getToken();
    this.tokenType = tokenAuth.getTokenAuthType();
    this.created = tokenAuth.getCreateDate();
    this.expired = tokenAuth.getExpired();
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public TokenAuthType getTokenType() {
    return tokenType;
  }

  public void setTokenType(
      TokenAuthType tokenType) {
    this.tokenType = tokenType;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public LocalDateTime getExpired() {
    return expired;
  }

  public void setExpired(LocalDateTime expired) {
    this.expired = expired;
  }
}
