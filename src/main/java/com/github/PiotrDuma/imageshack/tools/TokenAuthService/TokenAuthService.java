package com.github.PiotrDuma.imageshack.tools.TokenAuthService;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import java.util.Optional;
import java.util.stream.Stream;

public interface TokenAuthService {
  String createToken(String email, TokenAuthType tokenAuthType, int activeTimeInMinutes);
  boolean isTokenActive(String email, String token, TokenAuthType tokenAuthType);
  void deleteToken(String email, TokenAuthType tokenAuthType);
  Optional<TokenAuthDTO> findToken(String email, TokenAuthType tokenAuthType);
  Stream<TokenAuthDTO> getAllTokensByEmail(String email);
  Stream<TokenAuthDTO> getAllExpiredTokens();
}
