package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObjectFactory;
import com.github.PiotrDuma.imageshack.tools.TokenGenerator.TokenGenerator;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenAuthServiceImplTest {
  private static final String TOKEN_EMAIL = "email@imageshack.com";
  private static final TokenAuthType TOKEN_TYPE = TokenAuthType.ACCOUNT_CONFIRMATION;
  private static final String TOKEN_VALUE = "1a2b3c";
  @Mock
  private TokenAuthRepo repo;
  @Mock
  private TokenGenerator generator;
  @Mock
  private TokenObjectFactory tokenObjectFactory;
  @Mock
  private Clock clock;

  private TokenAuthService service;

  @BeforeEach
  void setUp(){
    this.service = new TokenAuthServiceImpl(repo, generator, tokenObjectFactory, clock);
  }
//TODO: add time/clock setup, add token values' validation
  @Test
  void saveMethodShouldCallRepoAndReturnValidObject(){
    TokenAuthDTO dto = new TokenAuthDTO(TOKEN_EMAIL, TOKEN_TYPE);
    TokenObject tokenObject = Mockito.mock(TokenObject.class);

    Mockito.when(generator.generate()).thenReturn(TOKEN_VALUE);
    Mockito.when(tokenObjectFactory.getTokenObject(dto)).thenReturn(tokenObject);
    Mockito.when(tokenObject.getEmail()).thenReturn(TOKEN_EMAIL);
    Mockito.when(tokenObject.getTokenType()).thenReturn(TOKEN_TYPE);
    Mockito.when(tokenObject.getTokenValue()).thenReturn(TOKEN_VALUE);

    TokenObject result = this.service.createToken(dto);
    Mockito.verify(this.tokenObjectFactory, Mockito.times(1)).getTokenObject(dto);
    Mockito.verify(this.generator, Mockito.times(1)).generate();
    Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TokenAuth.class));

    assertEquals(TOKEN_EMAIL, result.getEmail());
    assertEquals(TOKEN_VALUE, result.getTokenValue());
    assertEquals(TOKEN_TYPE, result.getTokenType());
  }

  @Test
  void saveMethodShouldInvokeServicesForDataAndObjects(){
    //TODO: check data collecting
  }

}