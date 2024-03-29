package com.github.PiotrDuma.imageshack.tools.TokenAuthService;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import java.time.Instant;
import java.util.stream.Stream;

public interface TokenAuthFacade {
  TokenObject create(TokenAuthDTO tokenObject) throws RuntimeException;
  boolean isValid(TokenObject tokenObject) throws RuntimeException;
  void  delete(TokenObject tokenObject) throws RuntimeException;
  Stream<TokenObject> find(String tokenValue);
  Stream<TokenObject> findByEmail(String email);
  void deleteExpiredTokens(String email);
  Instant expiresAt(TokenObject tokenObject) throws RuntimeException;
}
