package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.tools.token.api.TokenAuthService;
import com.github.PiotrDuma.imageshack.tools.token.api.TokenNotFoundException;
import com.github.PiotrDuma.imageshack.tools.token.api.TokenRequest;
import com.github.PiotrDuma.imageshack.tools.token.api.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthServiceTest {
    private static final String NOT_FOUND_EXCEPTION = "Provided token not found";
    private static final ZonedDateTime NOW = ZonedDateTime.of(
            2022,
            10,
            26,
            00,
            05,
            12,
            0,
            ZoneId.of("UTC"));

    @Mock
    private TokenRepository repo;
    @Mock
    private Clock clock;

    private TokenAuthService service;

    @BeforeEach
    void setUp(){
        this.service = new TokenAuthServiceImpl(repo, clock);
    }

    @Test
    void isValidShouldThrowNotFoundExceptionWhenRepoNotFoundToken(){
        TokenRequest request = new TokenRequest(UUID.randomUUID(), UUID.randomUUID(), TokenType.ACCOUNT_ACTIVATION);
        when(this.repo.findByPublicId(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(TokenNotFoundException.class, () -> this.service.isValid(request));

        assertEquals(NOT_FOUND_EXCEPTION, exception.getMessage());
    }

    @Test
    void isValidShouldThrowNotFoundExceptionWhenTokenValueIsInvalid(){
        TokenRequest request = new TokenRequest(UUID.randomUUID(), UUID.randomUUID(), TokenType.ACCOUNT_ACTIVATION);
        TokenEntity repoResponse = Mockito.mock(TokenEntity.class);

        when(repoResponse.getTokenValue()).thenReturn(UUID.randomUUID());
        when(this.repo.findByPublicId(any())).thenReturn(Optional.of(repoResponse));

        Exception exception = assertThrows(TokenNotFoundException.class, () -> this.service.isValid(request));

        assertEquals(NOT_FOUND_EXCEPTION, exception.getMessage());
    }

    @Test
    void isValidShouldThrowNotFoundExceptionWhenTokenTypeIsInvalid(){
        UUID tokenValue = UUID.randomUUID();
        TokenRequest request = new TokenRequest(UUID.randomUUID(), tokenValue, TokenType.ACCOUNT_ACTIVATION);
        TokenEntity repoResponse = Mockito.mock(TokenEntity.class);

        when(repoResponse.getTokenValue()).thenReturn(tokenValue);
        when(repoResponse.getTokenType()).thenReturn(TokenType.PASSWORD_RESET);
        when(this.repo.findByPublicId(any())).thenReturn(Optional.of(repoResponse));

        Exception exception = assertThrows(TokenNotFoundException.class, () -> this.service.isValid(request));

        assertEquals(NOT_FOUND_EXCEPTION, exception.getMessage());
    }

    @Test
    void isValidShouldReturnFalseWhenTokenIsExpired(){
        UUID tokenValue = UUID.randomUUID();
        TokenRequest request = new TokenRequest(UUID.randomUUID(), tokenValue, TokenType.ACCOUNT_ACTIVATION);
        TokenEntity repoResponse = Mockito.mock(TokenEntity.class);

        when(repoResponse.getTokenValue()).thenReturn(tokenValue);
        when(repoResponse.getTokenType()).thenReturn(TokenType.ACCOUNT_ACTIVATION);
        when(this.repo.findByPublicId(any())).thenReturn(Optional.of(repoResponse));
        when(repoResponse.isValid(any())).thenReturn(false);

        assertFalse(this.service.isValid(request));
    }

    @Test
    void isValidShouldReturnTrueWhenTokenRequestPassValidation(){
        UUID tokenValue = UUID.randomUUID();
        TokenRequest request = new TokenRequest(UUID.randomUUID(), tokenValue, TokenType.ACCOUNT_ACTIVATION);
        TokenEntity repoResponse = Mockito.mock(TokenEntity.class);

        when(repoResponse.getTokenValue()).thenReturn(tokenValue);
        when(repoResponse.getTokenType()).thenReturn(TokenType.ACCOUNT_ACTIVATION);
        when(this.repo.findByPublicId(any())).thenReturn(Optional.of(repoResponse));
        when(repoResponse.isValid(any())).thenReturn(true);

        assertTrue(this.service.isValid(request));
    }
}