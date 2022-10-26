package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;

public interface TokenObject {
  String getTokenValue();
  String getEmail();
  TokenAuthType getTokenType();
  int getTokenActiveTimeMinutes();
}
