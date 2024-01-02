package com.github.PiotrDuma.imageshack.tools.token.api;

import java.time.Instant;
import java.util.UUID;

public interface TokenObject {
  Long getTokenOwnerId();
  UUID getTokenId();
  UUID getTokenValue();
  TokenType getTokenType();
  Boolean isValid(Instant currentTime);
  Instant expiresAt();
}
