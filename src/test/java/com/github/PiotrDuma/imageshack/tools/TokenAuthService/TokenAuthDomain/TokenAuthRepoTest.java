package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@DataJpaTest
@Tag("IntegrationTest")
class TokenAuthRepoTest {

  private static final String TOKEN_EMAIL = "auth1@email.com";
  private static final String TOKEN_VALUE = "awdvfd12354364rsfxxcvv";
  private static final TokenAuthType TOKEN_AUTH_TYPE = TokenAuthType.PASSWORD_RESET;
  private TokenAuth exampleObject;
  @Autowired
  private TokenAuthRepo repo;

  @Mock
  private Clock clock;

  private static final ZonedDateTime NOW = ZonedDateTime.of(
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
  void getAllTokensTest(){
    TokenAuth token1 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(30, ChronoUnit.MINUTES));

    TokenAuth token2 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(30, ChronoUnit.MINUTES));

    repo.save(token1);
    repo.save(token2);

    List<TokenAuth> result = this.repo.getAllTokens();

    assertEquals(3, result.size());
    assertTrue(result.containsAll(List.of(token1, token2, this.exampleObject)));
  }

  @Test
  void deleteByEmailTest(){
    TokenAuth token1 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(60, ChronoUnit.MINUTES));
    TokenAuth token2 = new TokenAuth("123" + TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(30, ChronoUnit.MINUTES));

    this.repo.save(token1);
    this.repo.save(token2);

    this.repo.deleteByEmail(TOKEN_EMAIL);

    assertEquals(1, repo.getAllTokens().size());
    assertTrue(repo.getAllTokens().contains(token2));
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

  @Test
  void getTokenByEmailAndTokenValueShouldReturnEmptyOptionalWhenEmailIsIncorrect() {
    String email = "123" + TOKEN_EMAIL;
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndTokenValue(email, TOKEN_VALUE);

    assertTrue(result.isEmpty());
  }

  @Test
  void getTokenByEmailAndTokenValueShouldReturnEmptyOptionalWhenValueIsIncorrect() {
    String value = "123" + TOKEN_VALUE;
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndTokenValue(TOKEN_EMAIL, value);

    assertTrue(result.isEmpty());
  }

  @Test
  void getTokenByEmailAndTokenValueShouldReturnTokenOptional() {
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndTokenValue(TOKEN_EMAIL, TOKEN_VALUE);

    assertTrue(result.isPresent());
    assertEquals(TOKEN_EMAIL, result.get().getEmail());
    assertEquals(TOKEN_VALUE, result.get().getTokenValue());
    assertEquals(TOKEN_AUTH_TYPE, result.get().getTokenType());
  }

  @Test
  void getTokenByEmailValueAndTypeShouldReturnValidOptional() {
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE,
        TOKEN_AUTH_TYPE);

    assertTrue(result.isPresent());
    assertEquals(TOKEN_EMAIL, result.get().getEmail());
    assertEquals(TOKEN_VALUE, result.get().getTokenValue());
    assertEquals(TOKEN_AUTH_TYPE, result.get().getTokenType());
  }

  @Test
  void getTokenByEmailValueAndTypeShouldReturnEmptyOptionalWhenEmailIsInvalid() {
    String email = "123" + TOKEN_EMAIL;
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndValueAndType(email, TOKEN_VALUE,
        TOKEN_AUTH_TYPE);

    assertTrue(result.isEmpty());
  }

  @Test
  void getTokenByEmailValueAndTypeShouldReturnEmptyOptionalWhenValueIsInvalid() {
    String value = "123" + TOKEN_VALUE;
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, value,
        TOKEN_AUTH_TYPE);

    assertTrue(result.isEmpty());
  }

  @Test
  void getTokenByEmailValueAndTypeShouldReturnEmptyOptionalWhenTypeIsInvalid() {
    TokenAuthType type = TokenAuthType.ACCOUNT_CONFIRMATION;
    Optional<TokenAuth> result = this.repo.getTokenByEmailAndValueAndType(TOKEN_EMAIL, TOKEN_VALUE,
        type);

    assertTrue(result.isEmpty());
  }

  @Test
  void getTokenByValueShouldReturnOptionalOfTokenAuth(){
    Optional<TokenAuth> result = this.repo.getTokenByValue(TOKEN_VALUE);

    assertTrue(result.isPresent());
    assertEquals(TOKEN_VALUE, result.get().getTokenValue());
  }

  @Test
  void getTokenByValueShouldReturnEmptyOptionalWhenNothingWasFound(){
    Optional<TokenAuth> result = this.repo.getTokenByValue("123" + TOKEN_VALUE);

    assertTrue(result.isEmpty());
  }

  @Test
  void deleteExpiredTokensTest(){
    Instant futureTimestamp = clock.instant().plus(45, ChronoUnit.MINUTES);
    TokenAuth token1 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(60, ChronoUnit.MINUTES));
    TokenAuth token2 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        futureTimestamp);

    repo.save(token1);
    repo.save(token2);

    this.repo.deleteExpiredTokens(futureTimestamp);

    assertTrue(this.repo.getExpiredTokens(futureTimestamp).isEmpty());
    assertEquals(2, this.repo.getAllTokens().size());
  }

  @Test
  void deleteExpiredTokensByEmailTest(){
    Instant futureTimestamp = clock.instant().plus(45, ChronoUnit.MINUTES);
    //token1 and token2 are not expired
    TokenAuth token1 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(60, ChronoUnit.MINUTES));

    TokenAuth token2 = new

        TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        futureTimestamp);

    //token2 expired
    TokenAuth token3 = new TokenAuth("123" + TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        this.clock.instant().plus(30, ChronoUnit.MINUTES));

    repo.save(token1);
    repo.save(token2);
    repo.save(token3);

    this.repo.deleteExpiredTokens(futureTimestamp, TOKEN_EMAIL);

    assertFalse(this.repo.getExpiredTokens(futureTimestamp).isEmpty());
    assertEquals(3, this.repo.getAllTokens().size());
    assertTrue(this.repo.getAllTokens().containsAll(List.of(token1,token2, token3)));
  }

  @Test
  void deleteExpiredTokensByTokenTypeTest(){
    Instant futureTimestamp = clock.instant().plus(45, ChronoUnit.MINUTES);
    //expired, invalid type
    TokenAuth token1 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TokenAuthType.ACCOUNT_CONFIRMATION,
        this.clock.instant(),
        this.clock.instant().plus(30, ChronoUnit.MINUTES));
    //not expired, valid type
    TokenAuth token2 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TOKEN_AUTH_TYPE,
        this.clock.instant(),
        futureTimestamp);

    //not expired, invalid type
    TokenAuth token3 = new TokenAuth(TOKEN_EMAIL, TOKEN_VALUE, TokenAuthType.ACCOUNT_CONFIRMATION,
        this.clock.instant(),
        this.clock.instant().plus(60, ChronoUnit.MINUTES));

    repo.save(token1);
    repo.save(token2);
    repo.save(token3);

    this.repo.deleteExpiredTokens(futureTimestamp, TOKEN_AUTH_TYPE);

    assertFalse(this.repo.getExpiredTokens(futureTimestamp).isEmpty());
    assertEquals(3, this.repo.getAllTokens().size());
    assertTrue(this.repo.getAllTokens().containsAll(List.of(token1,token2, token3)));
  }
}