package com.github.PiotrDuma.imageshack.tools.TokenAuthService;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.AbstractTokenObject;
import java.util.stream.Stream;

public interface TokenAuthFacade {
  <T extends AbstractTokenObject> T create(T tokenObject);
  <T extends AbstractTokenObject> boolean isValid(T tokenObject) throws RuntimeException;
  <T extends AbstractTokenObject> void  delete(T tokenObject) throws RuntimeException;
  <T extends AbstractTokenObject> Stream<T> find(String tokenValue);
  <T extends AbstractTokenObject> Stream<T> findByEmail(String email);
  void deleteExpiredTokens(String email);
}
