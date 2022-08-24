package com.github.PiotrDuma.imageshack.tools.TokenGenerator;

import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("tokenGenerator")
class TokenGeneratorImpl implements TokenGenerator{
  //source https://stackoverflow.com/posts/56628391/revisions
  private final SecureRandom secureRandom;
  private final Base64.Encoder base64Encoder;

  public TokenGeneratorImpl() {
    this.secureRandom = new SecureRandom();
    this.base64Encoder = Base64.getUrlEncoder();
  }

  @Override
  public String generate() {
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return base64Encoder.encodeToString(randomBytes);
  }
}
