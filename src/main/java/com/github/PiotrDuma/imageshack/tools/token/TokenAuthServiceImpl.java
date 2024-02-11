package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.tools.token.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.temporal.ChronoUnit;

@Service
class TokenAuthServiceImpl implements TokenAuthService {
    private static final String NOT_FOUND = "Provided token not found";

    private final TokenRepository repository;
    private final Clock clock;

    @Autowired
    public TokenAuthServiceImpl(TokenRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public TokenObject create(TokenProvider provider) {
        TokenEntity token = new TokenEntity(provider.getOwnerId(),
                provider.getTokenType(),
                clock.instant(),
                clock.instant().plus(provider.getTokenActiveTimeMinutes(), ChronoUnit.MINUTES));
        return this.repository.save(token);
    }

    @Override
    public boolean isValid(TokenRequest token) throws TokenNotFoundException {
        TokenEntity tokenEntity = loadToken(token);
        validTokenParameters(tokenEntity, token);
        return tokenEntity.isValid(this.clock.instant());
    }

    private TokenEntity loadToken(TokenRequest token){
        return this.repository.findByPublicId(token.getTokenId())
                .orElseThrow(() -> new TokenNotFoundException(NOT_FOUND));
    }

    private void validTokenParameters(TokenEntity tokenEntity,TokenRequest tokenRequest){
        if(!tokenEntity.getTokenValue().equals(tokenRequest.getValue()) ||
                !tokenEntity.getTokenType().equals(tokenRequest.getTokenType())){
            throw new TokenNotFoundException(NOT_FOUND);
        }
    }
}
