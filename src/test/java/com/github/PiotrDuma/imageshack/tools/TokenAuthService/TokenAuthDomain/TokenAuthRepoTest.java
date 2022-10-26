package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:test")
class TokenAuthRepoTest {

  private static String TOKEN_EMAIL = "auth1@email.com";
  private static String TOKEN_VALUE = "awdvfd12354364rsfxxcvv";
  private static TokenAuthType TOKEN_AUTH_TYPE = TokenAuthType.PASSWORD_RESET;
  private TokenAuth exampleObject;
  @Autowired
  private TokenAuthRepo repo;

  @Mock
  private Clock clock;

  private static ZonedDateTime NOW = ZonedDateTime.of(
      2022,
      10,
      26,
      00,
      05,
      12,
      0,
      ZoneId.of("UTC"));

  @BeforeEach
  void setUp() {
    Mockito.when(clock.getZone()).thenReturn(NOW.getZone());
    Mockito.when(clock.instant()).thenReturn(NOW.toInstant());
    repo.deleteAll();

    this.exampleObject = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(30, ChronoUnit.MINUTES));

    repo.save(exampleObject);
  }

  @Test
  void shouldReturnOneExpiredTokenWhenIsOutOfDate() {
    Instant now = this.clock.instant().plus(31, ChronoUnit.MINUTES);
    List<TokenAuth> result = repo.getExpiredTokens(now);

    assertEquals(1, result.size());
    assertTrue(result.contains(exampleObject));
  }

  @Test
  void shouldReturnEmptyListWhenThereIsNoExpiredTokens() {
    Instant now = this.clock.instant().plus(22, ChronoUnit.MINUTES);
    List<TokenAuth> result = repo.getExpiredTokens(now);

    assertTrue(result.isEmpty());
  }
}