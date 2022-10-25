package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.AbstractTokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

interface TokenAuthService <T extends AbstractTokenObject> {
  T createToken(TokenAuthDTO tokenAuthDTO);
  boolean isActive(T tokenObject);
  Instant expiresAt(T tokenObject) throws TokenAuthNotFoundException;
  void delete(T tokenObject) throws TokenAuthNotFoundException;
  void delete(String email) throws TokenAuthNotFoundException;
  void delete(String tokenValue, TokenAuthType tokenType) throws TokenAuthNotFoundException;
  void deleteExpired();
  void deleteExpired(String email);
  void deleteExpired(TokenAuthType tokenType);
  Optional<T> findToken(String tokenValue);
  Stream<T> getAllTokensByEmail(String email);
  Stream<T> getAllExpiredTokens();
  Stream<T> getAllExpiredTokens(String email);
}
