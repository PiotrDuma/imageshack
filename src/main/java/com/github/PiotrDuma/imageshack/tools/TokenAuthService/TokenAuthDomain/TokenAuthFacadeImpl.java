package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.AbstractTokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import java.util.stream.Stream;

class TokenAuthFacadeImpl implements TokenAuthFacade {

  @Override
  public <T extends AbstractTokenObject> T create(T tokenObject) {
    return null;
  }

  @Override
  public <T extends AbstractTokenObject> boolean isValid(T tokenObject) throws RuntimeException {
    return false;
  }

  @Override
  public <T extends AbstractTokenObject> void delete(T tokenObject) throws RuntimeException {

  }

  @Override
  public <T extends AbstractTokenObject> Stream<T> find(String tokenValue) {
    return null;
  }

  @Override
  public <T extends AbstractTokenObject> Stream<T> findByEmail(String email) {
    return null;
  }

  @Override
  public void deleteExpiredTokens(String email) {

  }
}
