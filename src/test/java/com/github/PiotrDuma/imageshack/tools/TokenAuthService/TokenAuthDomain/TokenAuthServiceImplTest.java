package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthNotFoundException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObjectFactory;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
  private static final String TOKEN_NOT_FOUND_VALUES = "Token not found. Cannot find token with "
      + "email address: %s and value: %s.";
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

  @Test
  void isActiveShouldReturnTrueIfTokenIsNotExpired(){
    TokenObject input = mockTokenObject();
    TokenAuth token = Mockito.mock(TokenAuth.class);
    Instant tokenExpireTime = NOW.toInstant().plus(5, ChronoUnit.MINUTES);

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.of(token));
    Mockito.when(token.getExpiredDateTime()).thenReturn(tokenExpireTime);
    Mockito.when(clock.instant()).thenReturn(NOW.toInstant());

    boolean result = this.service.isActive(input);

    assertTrue(result);
    assertEquals(tokenExpireTime, token.getExpiredDateTime());
    assertFalse(NOW.toInstant().isAfter(tokenExpireTime));
  }

  @Test
  void isActiveShouldReturnFalseIfTokenIsExpired(){
    TokenObject input = mockTokenObject();
    TokenAuth token = Mockito.mock(TokenAuth.class);
    Instant tokenExpireTime = NOW.toInstant().minus(5, ChronoUnit.MINUTES);

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.of(token));
    Mockito.when(token.getExpiredDateTime()).thenReturn(tokenExpireTime);
    Mockito.when(clock.instant()).thenReturn(NOW.toInstant());

    boolean result = this.service.isActive(input);

    assertFalse(result);
    assertEquals(tokenExpireTime, token.getExpiredDateTime());
    assertTrue(NOW.toInstant().isAfter(tokenExpireTime));
  }

  @Test
  void isActiveShouldThrowExceptionWhenTokenIsNotFound(){
    TokenObject input = mockTokenObject();

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.empty());

    Exception result = assertThrows(TokenAuthNotFoundException.class,
              () -> this.service.isActive(input));

    Mockito.verify(this.repo, Mockito.times(1))
        .getTokenByEmailAndValueAndType(Mockito.anyString(), Mockito.anyString(), Mockito.any(TokenAuthType.class));
    assertEquals(String.format(TOKEN_NOT_FOUND_VALUES, TOKEN_EMAIL, TOKEN_VALUE), result.getMessage());
  }

  @Test
  void expiresAtShouldReturnInstantOfTokenAuth(){
    TokenObject input = mockTokenObject();
    TokenAuth token = Mockito.mock(TokenAuth.class);

    Mockito.when(token.getExpiredDateTime()).thenReturn(NOW.toInstant());
    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.of(token));

    Instant result = this.service.expiresAt(input);

    Mockito.verify(this.repo, Mockito.times(1))
        .getTokenByEmailAndValueAndType(Mockito.anyString(), Mockito.anyString(), Mockito.any(TokenAuthType.class));
    assertEquals(NOW.toInstant(), result);
  }

  @Test
  void expiresAtShouldThrowWhenTokenNotFound(){
    TokenObject input = mockTokenObject();

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.empty());

    Exception result = assertThrows(TokenAuthNotFoundException.class,
        () -> this.service.expiresAt(input));

    Mockito.verify(this.repo, Mockito.times(1))
        .getTokenByEmailAndValueAndType(Mockito.anyString(), Mockito.anyString(), Mockito.any(TokenAuthType.class));
    assertEquals(String.format(TOKEN_NOT_FOUND_VALUES, TOKEN_EMAIL, TOKEN_VALUE), result.getMessage());
  }

  @Test
  void deleteWithTokenObjectShouldInvokeRepoDeleteWhenTokenIsFound(){
    TokenObject input = mockTokenObject();
    TokenAuth token = Mockito.mock(TokenAuth.class);

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.of(token));

    this.service.delete(input);
    Mockito.verify(this.repo, Mockito.times(1)).delete(token);
  }

  @Test
  void deleteWithTokenObjectShouldThrowWhenTokenIsNotFound(){
    TokenObject input = mockTokenObject();

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_TYPE))
        .thenReturn(Optional.empty());

    Exception result = assertThrows(TokenAuthNotFoundException.class,
        () -> this.service.delete(input));

    Mockito.verify(this.repo, Mockito.times(1))
        .getTokenByEmailAndValueAndType(Mockito.anyString(), Mockito.anyString(), Mockito.any(TokenAuthType.class));
    assertEquals(String.format(TOKEN_NOT_FOUND_VALUES, TOKEN_EMAIL, TOKEN_VALUE), result.getMessage());
  }

  @Test
  void deleteByEmailShouldRemoveAllTokensReturnedFromRepo(){
    TokenAuth token1 = Mockito.mock(TokenAuth.class);
    TokenAuth token2 = Mockito.mock(TokenAuth.class);
    TokenAuth token3 = Mockito.mock(TokenAuth.class);

    List<TokenAuth> output = List.of(token1, token2, token3);

    Mockito.when(this.repo.getTokensByEmail(TOKEN_EMAIL)).thenReturn(output);

    this.service.delete(TOKEN_EMAIL);
    Mockito.verify(this.repo, Mockito.times(1)).getTokensByEmail(Mockito.anyString());
    Mockito.verify(this.repo, Mockito.times(3)).delete(Mockito.any(TokenAuth.class));
    Mockito.verify(this.repo, Mockito.times(1)).delete(token1);
    Mockito.verify(this.repo, Mockito.times(1)).delete(token2);
    Mockito.verify(this.repo, Mockito.times(1)).delete(token3);
  }

  @Test
  void presentShouldReturnTrueIfTokenExists(){
    TokenObject input = mockTokenObject();
    TokenAuth output = Mockito.mock(TokenAuth.class);

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(Mockito.anyString(),
        Mockito.anyString(), Mockito.any(TokenAuthType.class)))
        .thenReturn(Optional.of(output));

    boolean result = this.service.present(input);

    assertTrue(result);
  }

  @Test
  void presentShouldReturnTrueIfTokenNotExists(){
    TokenObject input = mockTokenObject();
    TokenAuth output = Mockito.mock(TokenAuth.class);

    Mockito.when(this.repo.getTokenByEmailAndValueAndType(Mockito.anyString(),
            Mockito.anyString(), Mockito.any(TokenAuthType.class)))
        .thenReturn(Optional.empty());

    boolean result = this.service.present(input);

    assertFalse(result);
  }

  private TokenObject mockTokenObject(){
    TokenObject token = Mockito.mock(TokenObject.class);
    Mockito.when(token.getTokenValue()).thenReturn(TOKEN_VALUE);
    Mockito.when(token.getEmail()).thenReturn(TOKEN_EMAIL);
    Mockito.when(token.getTokenType()).thenReturn(TOKEN_TYPE);
    return token;
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