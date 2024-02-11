package com.github.PiotrDuma.imageshack.tools.token.api;

public interface TokenAuthService {
  TokenObject create(TokenProvider provider);
  boolean isValid(TokenRequest token) throws TokenNotFoundException;
}
