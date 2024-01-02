package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.tools.token.api.TokenType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Tag("IntegrationTest")
class TokenAuthRepoTest {
  private static final Long OWNER_ID = 1L;
  private static final TokenType TOKEN_TYPE = TokenType.PASSWORD_RESET;

  private static final ZonedDateTime NOW = ZonedDateTime.of(
      2022,
      10,
      26,
      00,
      05,
      12,
      0,
      ZoneId.of("UTC"));
  private static final Instant TOKEN_CREATED = NOW.toInstant();
  private static final Instant TOKEN_EXPIRES = NOW.toInstant().plus(60, ChronoUnit.MINUTES);

  @Autowired
  private TokenRepository repo;

  @Test
  void shouldSaveTokenToDatabase(){
    TokenEntity tokenEntity = new TokenEntity(OWNER_ID, TOKEN_TYPE, TOKEN_CREATED, TOKEN_EXPIRES);

    assertFalse(repo.findByPublicId(tokenEntity.getTokenId()).isPresent());
    repo.save(tokenEntity);
    assertTrue(repo.findByPublicId(tokenEntity.getTokenId()).isPresent());
  }

  @Test
  void shouldFindEqualTokenInDatabase(){
    TokenEntity tokenEntity = new TokenEntity(OWNER_ID, TOKEN_TYPE, TOKEN_CREATED, TOKEN_EXPIRES);


    repo.save(tokenEntity);
    Optional<TokenEntity> result = this.repo.findByPublicId(tokenEntity.getTokenId());
    assertTrue(result.isPresent());
    assertEquals(OWNER_ID, result.get().getTokenOwnerId());
    assertEquals(TOKEN_TYPE, result.get().getTokenType());
    assertEquals(tokenEntity.getTokenValue(), result.get().getTokenValue());
    assertEquals(TOKEN_EXPIRES, result.get().expiresAt());
  }
}