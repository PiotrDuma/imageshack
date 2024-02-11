package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.tools.token.api.TokenObject;
import com.github.PiotrDuma.imageshack.tools.token.api.TokenType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "token")
class TokenEntity implements Serializable, TokenObject {

  @Id
  @Column(name = "token_id", nullable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(name = "public_id", unique = true)
  private UUID publicId;

  @NotNull
  @Column(name = "owner_id")
  private Long ownerId;

  @NotNull
  @Column(name = "value", unique = true)
  private UUID value;

  @NotNull
  @Column(name = "token_type")
  @Enumerated(EnumType.STRING)
  private TokenType tokenType;

  @NotNull
  @Column(name = "created")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant createdAt;

  @NotNull
  @Column(name = "expires")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant expiresAt;

  protected TokenEntity() {
  }

  protected TokenEntity(Long ownerId, TokenType tokentype, Instant createdAt, Instant expiresAt) {
    this.publicId = UUID.randomUUID();
    this.value = UUID.randomUUID();
    this.ownerId = ownerId;
    this.tokenType = tokentype;
    this.createdAt = createdAt;
    this.expiresAt = expiresAt;
  }

  @Override
  public UUID getTokenValue() {
    return this.value;
  }

  @Override
  public UUID getTokenId() {
    return this.publicId;
  }

  @Override
  public Long getTokenOwnerId() {
    return this.ownerId;
  }

  public Boolean isValid(Instant currentTime) {
    return !currentTime.isAfter(this.expiresAt);
  }

  @Override
  public Instant expiresAt() {
    return this.expiresAt;
  }

  @Override
  public TokenType getTokenType() {
    return this.tokenType;
  }

  protected Instant getCreateDateTime() {
    return createdAt;
  }
}
