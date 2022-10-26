package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenGenerator.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenObjectFactory {
  private final static String NULL_TYPE = "TokenAuthDTO has no token type: token auth type is null.";
  private static final String UNKNOWN_TYPE = "Cannot generate TokenObject. Unknown token type: %s.";
  private final TokenGenerator generator;

  @Autowired
  public TokenObjectFactory(TokenGenerator generator) {
    this.generator = generator;
  }

  public TokenObject getTokenObject(TokenAuthDTO tokenAuthDTO) {
    TokenAuthType tokenType = tokenAuthDTO.getTokenType();
    if (tokenType == null) {
      throw new RuntimeException(NULL_TYPE);
    }
    switch (tokenType) {
      case PASSWORD_RESET:
        return new PasswordResetTokenObject(tokenAuthDTO.getEmail(), this.generator.generate());
      case ACCOUNT_CONFIRMATION:
        return new AccountConfirmationTokenObject(tokenAuthDTO.getEmail(), this.generator.generate());
    }
    throw new RuntimeException(UNKNOWN_TYPE + tokenType);
  }
}