package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthNotFoundException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

interface TokenAuthService {
  TokenObject createToken(TokenAuthDTO tokenAuthDTO);
  boolean isActive(TokenObject tokenObject);
  Instant expiresAt(TokenObject tokenObject) throws TokenAuthNotFoundException;
  void delete(TokenObject tokenObject) throws TokenAuthNotFoundException;
  void delete(String email) throws TokenAuthNotFoundException;
  void delete(String tokenValue, TokenAuthType tokenType) throws TokenAuthNotFoundException;
  void deleteExpired();
  void deleteExpired(String email);
  void deleteExpired(TokenAuthType tokenType);
  Optional<TokenObject> findToken(String tokenValue);
  Stream<TokenObject> getAllTokensByEmail(String email);
  Stream<TokenObject> getAllExpiredTokens();
  Stream<TokenObject> getAllExpiredTokens(String email);
  boolean present(TokenObject tokenObject);
}
