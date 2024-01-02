package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.tools.token.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
class TokenAuthServiceImpl implements TokenAuthService {
    private static final String NOT_FOUND = "Token not found";

    private final TokenRepository repository;
    private final Clock clock;

    @Autowired
    public TokenAuthServiceImpl(TokenRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public TokenObject create(TokenProvider provider) {
        return null; //TODO:
    }

    @Override
    public TokenObject loadToken(TokenRequest token) throws TokenNotFoundException {
        return null; //TODO:
    }
}
