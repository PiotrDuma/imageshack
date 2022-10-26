package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
class TokenAuthFacadeImpl implements TokenAuthFacade {
  private final TokenAuthService service;

  public TokenAuthFacadeImpl(TokenAuthService service) {
    this.service = service;
  }

  @Override
  public TokenObject create(TokenObject tokenObject) {
    return null;
  }

  @Override
  public boolean isValid(TokenObject tokenObject) throws RuntimeException {
    return false;
  }

  @Override
  public void delete(TokenObject tokenObject) throws RuntimeException {

  }

  @Override
  public Stream<TokenObject> find(String tokenValue) {
    return null;
  }

  @Override
  public Stream<TokenObject> findByEmail(String email) {
    return null;
  }

  @Override
  public void deleteExpiredTokens(String email) {

  }
}
