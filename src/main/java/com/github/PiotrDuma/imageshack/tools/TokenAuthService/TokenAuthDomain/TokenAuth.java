package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
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

//TODO:refactoring.
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
  @NotEmpty
  @Column(name = "token_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private TokenAuthType tokenAuthType;

  @Column(name = "created", nullable = false)
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createDate; //TODO: refactor localdatetime to Instant.

  @Column(name = "expired", nullable = false)
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime expired;

  protected TokenAuth() {
  }

  public TokenAuth(String email, String token, TokenAuthType tokenAuthType,
      LocalDateTime createDate,
      LocalDateTime expired) {
    this.email = email;
    this.token = token;
    this.tokenAuthType = tokenAuthType;
    this.createDate = createDate;
    this.expired = expired;
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

  public LocalDateTime getCreateDate() {
    return createDate;
  }

  public void setCreateDate(LocalDateTime createDate) {
    this.createDate = createDate;
  }

  public LocalDateTime getExpired() {
    return expired;
  }

  public void setExpired(LocalDateTime expired) {
    this.expired = expired;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
