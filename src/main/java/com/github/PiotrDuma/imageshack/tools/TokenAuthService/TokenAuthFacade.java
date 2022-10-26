package com.github.PiotrDuma.imageshack.tools.TokenAuthService;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.AbstractTokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import java.util.stream.Stream;

public interface TokenAuthFacade {
  TokenObject create(TokenObject tokenObject);
  boolean isValid(TokenObject tokenObject) throws RuntimeException;
  void  delete(TokenObject tokenObject) throws RuntimeException;
  Stream<TokenObject> find(String tokenValue);
  Stream<TokenObject> findByEmail(String email);
  void deleteExpiredTokens(String email);
}
