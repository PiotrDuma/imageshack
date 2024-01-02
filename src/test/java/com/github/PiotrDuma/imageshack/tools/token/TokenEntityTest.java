package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.tools.token.api.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TokenEntityTest {
    private static final Long OWNER_ID = 1L;
    private static final TokenType TOKEN_TYPE = TokenType.ACCOUNT_ACTIVATION;
    private static final ZonedDateTime TIMESTAMP_CREATED = ZonedDateTime.of(
            2022,
            11,
            04,
            16,
            05,
            12,
            0,
            ZoneId.of("UTC"));
    private static final ZonedDateTime TIMESTAMP_EXPIRES = TIMESTAMP_CREATED.plusMinutes(60);

    private TokenEntity token;

    @BeforeEach
    void setUp() {
        this.token = new TokenEntity(OWNER_ID, TOKEN_TYPE, TIMESTAMP_CREATED.toInstant(), TIMESTAMP_EXPIRES.toInstant());
    }

    @Test
    void shouldCreateTokenWithProvidedValues() {
        assertEquals(OWNER_ID, token.getTokenOwnerId());
        assertEquals(TOKEN_TYPE, token.getTokenType());
        assertEquals(TIMESTAMP_CREATED.toInstant(), token.getCreateDateTime());
        assertEquals(TIMESTAMP_EXPIRES.toInstant(), token.expiresAt());
    }

    @Test
    void isValidShouldReturnTrueIfExpiresDateTimeIsAheadOfCurrentTime() {
        Instant timestamp = TIMESTAMP_EXPIRES.minusSeconds(1).toInstant();

        assertTrue(token.isValid(timestamp));
    }

    @Test
    void isValidShouldReturnTrueIfExpiresDateTimeAndCurrentTimeAreEquals() {
        Instant timestamp = TIMESTAMP_EXPIRES.toInstant();

        assertTrue(token.isValid(timestamp));
    }

    @Test
    void isValidShouldReturnFalseIfExpiresDateTimeIsBehindCurrentTime() {
        Instant timestamp = TIMESTAMP_EXPIRES.plusSeconds(1).toInstant();

        assertFalse(token.isValid(timestamp));
    }
}