package com.github.PiotrDuma.imageshack.tools.token.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {
    private static final int DEFAULT_TOKEN_TIME = 60;
    private static final TokenType DEFAULT_TOKEN_TYPE = TokenType.AUTHENTICATION;

    @Test
    void shouldBuildObjectWithDefaultParameters(){
        Long ownerId = 1L;

        TokenProvider token = new TokenProvider.TokenBuilder(ownerId).build();

        assertEquals(ownerId, token.getOwnerId());
        assertEquals(DEFAULT_TOKEN_TIME, token.getTokenActiveTimeMinutes());
        assertEquals(DEFAULT_TOKEN_TYPE, token.getTokenType());
    }

    @Test
    void shouldBuildObjectWithAdditionalParameters(){
        Long ownerId = 1L;
        TokenType expectedTokenType = TokenType.PASSWORD_RESET;
        int expectedTime = 22;

        TokenProvider token = new TokenProvider.TokenBuilder(ownerId)
                .setTokenType(expectedTokenType)
                .setTokenActiveTimeMinutes(expectedTime)
                .build();

        assertEquals(ownerId, token.getOwnerId());
        assertEquals(expectedTime, token.getTokenActiveTimeMinutes());
        assertEquals(expectedTokenType, token.getTokenType());
    }
}