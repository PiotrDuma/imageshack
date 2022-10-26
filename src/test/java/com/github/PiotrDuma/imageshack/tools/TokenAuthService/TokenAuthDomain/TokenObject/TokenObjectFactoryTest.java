package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenGenerator.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenObjectFactoryTest {
  @Mock
  private TokenGenerator generator;
  private TokenObjectFactory tokenObjectFactory;

  private static final String TOKEN_VALUE = "12345vfVf";
  private static final String TOKEN_EMAIL = "e@mail.com";

  @BeforeEach
  void setUp(){
    this.tokenObjectFactory = new TokenObjectFactory(this.generator);
  }

  @Test
  void accConfirmTypeShouldReturnAccountConfirmationTokenObject(){
    TokenAuthType tokenType = TokenAuthType.ACCOUNT_CONFIRMATION;
    TokenAuthDTO input = new TokenAuthDTO(TOKEN_EMAIL, tokenType);

    Mockito.when(this.generator.generate()).thenReturn(TOKEN_VALUE);
    TokenObject result = this.tokenObjectFactory.getTokenObject(input);

    //then
    assertEquals(TOKEN_VALUE, result.getTokenValue());
    assertEquals(TOKEN_EMAIL, result.getEmail());
    assertEquals(tokenType, result.getTokenType());
  }

  @Test
  void passResetTypeShouldReturnPasswordResetTokenObject(){
    TokenAuthType tokenType = TokenAuthType.PASSWORD_RESET;
    TokenAuthDTO input = new TokenAuthDTO(TOKEN_EMAIL, tokenType);

    Mockito.when(this.generator.generate()).thenReturn(TOKEN_VALUE);
    TokenObject result = this.tokenObjectFactory.getTokenObject(input);

    //then
    assertEquals(TOKEN_VALUE, result.getTokenValue());
    assertEquals(TOKEN_EMAIL, result.getEmail());
    assertEquals(tokenType, result.getTokenType());
  }

  @Test
  void nullInputShouldThrowNullValueRuntimeException(){
    String message = "TokenAuthDTO has no token type: token auth type is null.";
    TokenAuthDTO input = new TokenAuthDTO(TOKEN_EMAIL, null);

    Exception result = assertThrows(RuntimeException.class, () ->this.tokenObjectFactory.getTokenObject(input));

    assertEquals(message, result.getMessage());
  }

}