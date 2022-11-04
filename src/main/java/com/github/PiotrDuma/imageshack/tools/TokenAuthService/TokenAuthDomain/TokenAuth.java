package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "tokens")
class TokenAuth implements Serializable {

  @Id
  @Column(name = "token_id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Email
  @NotNull
  @NotEmpty
  @Column(name = "email", nullable = false)
  private String email;

  @NotEmpty
  @NotNull
  @Column(name = "token", nullable = false)
  private String token;

  @NotNull
  @Column(name = "token_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TokenAuthType tokenAuthType;

  @Column(name = "created", nullable = false)
  @JsonFormat(shape = Shape.STRING ,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  private Instant createDateTime;

  @Column(name = "expired", nullable = false)
  @JsonFormat(shape = Shape.STRING ,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant expiredDateTime;

  protected TokenAuth() {
  }

  protected TokenAuth(String email, String token, TokenAuthType tokenAuthType,
      Instant createDateTime,
      Instant expiredDateTime) {
    this.email = email;
    this.token = token;
    this.tokenAuthType = tokenAuthType;
    this.createDateTime = createDateTime;
    this.expiredDateTime = expiredDateTime;
  }

  protected TokenAuth(TokenObject tokenObject, Instant createDateTime, Instant expiredDateTime) {
    this.email = tokenObject.getEmail();
    this.token = tokenObject.getTokenValue();
    this.tokenAuthType = tokenObject.getTokenType();
    this.createDateTime = createDateTime;
    this.expiredDateTime = expiredDateTime;
  }

  public Long getId() {
    return id;
  }

  protected void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public TokenAuthType getTokenAuthType() {
    return tokenAuthType;
  }

  public void setTokenAuthType(
      TokenAuthType tokenAuthType) {
    this.tokenAuthType = tokenAuthType;
  }

  public Instant getCreateDateTime() {
    return createDateTime;
  }

  public void setCreateDateTime(Instant createDateTime) {
    this.createDateTime = createDateTime;
  }

  public Instant getExpiredDateTime() {
    return expiredDateTime;
  }

  public void setExpiredDateTime(Instant expiredDateTime) {
    this.expiredDateTime = expiredDateTime;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TokenAuth tokenAuth = (TokenAuth) o;
    return id.equals(tokenAuth.id) && email.equals(tokenAuth.email) && token.equals(tokenAuth.token)
        && tokenAuthType == tokenAuth.tokenAuthType && createDateTime.equals(
        tokenAuth.createDateTime)
        && expiredDateTime.equals(tokenAuth.expiredDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, token, tokenAuthType, createDateTime, expiredDateTime);
  }
}
