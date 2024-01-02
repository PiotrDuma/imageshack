package com.github.PiotrDuma.imageshack.tools.token.api;

public interface TokenAuthService {
  TokenObject create(TokenProvider provider);
  TokenObject loadToken(TokenRequest token) throws TokenNotFoundException;
}
