package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import static org.junit.jupiter.api.Assertions.*;

import antlr.Token;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObjectFactory;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
  private TokenObjectFactory tokenObjectFactory;
  @Mock
  private Clock clock;

  private TokenAuthService service;

  private static ZonedDateTime NOW = ZonedDateTime.of(
      2022,
      11,
      04,
      16,
      05,
      12,
      0,
      ZoneId.of("UTC"));

  @BeforeEach
  void setUp(){
    this.service = new TokenAuthServiceImpl(repo, tokenObjectFactory, clock);
  }
//TODO: add time/clock setup, add token values' validation
  @Test
  void createTokenMethodShouldCallRepoAndReturnValidObject(){
    TokenAuthDTO dto = new TokenAuthDTO(TOKEN_EMAIL, TOKEN_TYPE);
    TokenAuth mockTokenAuth = Mockito.mock(TokenAuth.class);
    TokenObject tokenObject = mockTokenObjectFactory(dto);

    Mockito.when(clock.instant()).thenReturn(NOW.toInstant());
    Mockito.when(tokenObject.getTokenActiveTimeMinutes()).thenReturn(30);

    TokenObject result = this.service.createToken(dto);
    Mockito.verify(this.repo, Mockito.times(1)).save(Mockito.any(TokenAuth.class));
    assertEquals(TOKEN_EMAIL, result.getEmail());
    assertEquals(TOKEN_VALUE, result.getTokenValue());
    assertEquals(TOKEN_TYPE, result.getTokenType());
  }

  @Test
  void createTokenMethodShouldInvokeServicesForDataAndObjects(){
    TokenAuthDTO dto = new TokenAuthDTO(TOKEN_EMAIL, TOKEN_TYPE);
    TokenObject tokenObject = Mockito.mock(TokenObject.class);
    Mockito.when(clock.instant()).thenReturn(NOW.toInstant());
    Mockito.when(tokenObjectFactory.getTokenObject(dto)).thenReturn(tokenObject);

    TokenObject result = this.service.createToken(dto);

    Mockito.verify(this.tokenObjectFactory, Mockito.times(1)).getTokenObject(dto);
    Mockito.verify(this.clock, Mockito.times(1)).instant();
  }

  @Test
  void createTokenMethodShouldInitializeTokenAuthWithValidValues(){
    TokenAuthDTO dto = new TokenAuthDTO(TOKEN_EMAIL, TOKEN_TYPE);
    TokenObject tokenObject = mockTokenObjectFactory(dto);

    //when
    Mockito.when(tokenObject.getTokenActiveTimeMinutes()).thenReturn(30);
    Mockito.when(clock.instant()).thenReturn(NOW.toInstant());
    Instant currentTime = clock.instant();
    Instant expiredTime = currentTime.plus(tokenObject.getTokenActiveTimeMinutes(), ChronoUnit.MINUTES);

    ArgumentCaptor<TokenAuth> argument = ArgumentCaptor.forClass(TokenAuth.class);

    //then
    TokenObject result = this.service.createToken(dto);
    Mockito.verify(this.repo).save(argument.capture());

    assertEquals(currentTime, argument.getValue().getCreateDateTime());
    assertEquals(expiredTime, argument.getValue().getExpiredDateTime());
    assertEquals(TOKEN_EMAIL, result.getEmail());
    assertEquals(TOKEN_VALUE, result.getTokenValue());
    assertEquals(TOKEN_TYPE, result.getTokenType());
  }

  private TokenObject mockTokenObjectFactory(TokenAuthDTO dto){
    TokenObject tokenObject = Mockito.mock(TokenObject.class);
    Mockito.when(tokenObjectFactory.getTokenObject(dto)).thenReturn(tokenObject);
    Mockito.when(tokenObject.getEmail()).thenReturn(TOKEN_EMAIL);
    Mockito.when(tokenObject.getTokenType()).thenReturn(TOKEN_TYPE);
    Mockito.when(tokenObject.getTokenValue()).thenReturn(TOKEN_VALUE);
    return tokenObject;
  }
}