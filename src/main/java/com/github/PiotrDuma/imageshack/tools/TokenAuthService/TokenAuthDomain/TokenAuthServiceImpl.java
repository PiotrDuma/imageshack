package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthNotFoundException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObjectFactory;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO: REFACTORING, ADD TESTS
@Service
@Transactional
class TokenAuthServiceImpl implements TokenAuthService {
    private static final String NOT_FOUND = "Token not found.";
    private static final String NOT_FOUND_BY_EMAIL_AND_VALUE = "Token not found. Cannot find "
        + "token with email address: %s and value: %s.";
    private final TokenAuthRepo tokenAuthRepo;
    private final TokenObjectFactory tokenObjectFactory;
    private final Clock clock;

    @Autowired
    public TokenAuthServiceImpl(TokenAuthRepo tokenAuthRepo, TokenObjectFactory tokenObjectFactory,
        Clock clock) {
        this.tokenAuthRepo = tokenAuthRepo;
        this.tokenObjectFactory = tokenObjectFactory;
        this.clock = clock;
    }

    @Override
    @Transactional
    public TokenObject createToken(TokenAuthDTO tokenAuthDTO) {
        TokenObject tokenObject = this.tokenObjectFactory.getTokenObject(tokenAuthDTO);
        Instant currentTime = this.clock.instant();
        Instant expiredTime = currentTime.plus(tokenObject.getTokenActiveTimeMinutes(), ChronoUnit.MINUTES);
        TokenAuth token = new TokenAuth(tokenObject, currentTime, expiredTime);
        this.tokenAuthRepo.save(token);
        return tokenObject;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isActive(TokenObject tokenObject) {
        Optional<TokenAuth> token = this.tokenAuthRepo.getTokenByEmailAndValueAndType(
            tokenObject.getEmail(), tokenObject.getTokenValue(), tokenObject.getTokenType());
        if(!token.isPresent()){
            throw new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL_AND_VALUE,
                tokenObject.getEmail(), tokenObject.getTokenValue()));
        }
        return token.get().getExpiredDateTime().isAfter(clock.instant())?true:false;
    }

    @Override
    @Transactional(readOnly = true)
    public Instant expiresAt(TokenObject tokenObject) throws TokenAuthNotFoundException {
        Optional<TokenAuth> token = this.tokenAuthRepo.getTokenByEmailAndValueAndType(
            tokenObject.getEmail(), tokenObject.getTokenValue(), tokenObject.getTokenType());
        if(!token.isPresent()){
            throw new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL_AND_VALUE,
                tokenObject.getEmail(), tokenObject.getTokenValue()));
        }
        return token.get().getExpiredDateTime();
    }

    @Override
    public void delete(TokenObject tokenObject) throws TokenAuthNotFoundException {
        Optional<TokenAuth> token = this.tokenAuthRepo.getTokenByEmailAndValueAndType(
            tokenObject.getEmail(), tokenObject.getTokenValue(), tokenObject.getTokenType());
        if(!token.isPresent()){
            throw new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL_AND_VALUE,
                tokenObject.getEmail(), tokenObject.getTokenValue()));
        }
        this.tokenAuthRepo.delete(token.get());
    }

    @Override
    public void delete(String email) throws TokenAuthNotFoundException {
        this.tokenAuthRepo.deleteByEmail(email);
    }

    @Override
    public void delete(String tokenValue, TokenAuthType tokenType)
        throws TokenAuthNotFoundException {
        Optional<TokenAuth> token = this.tokenAuthRepo.getTokenByValue(tokenValue);
        if(!token.isPresent() || !token.get().getTokenType().equals(tokenType)){
            throw new TokenAuthNotFoundException(NOT_FOUND);
        }
        this.tokenAuthRepo.delete(token.get());
    }

    @Override
    public void deleteExpired() {
        this.tokenAuthRepo.deleteExpiredTokens(clock.instant());
    }

    @Override
    public void deleteExpired(String email) {
        this.tokenAuthRepo.deleteExpiredTokens(clock.instant(), email);
    }

    @Override
    public void deleteExpired(TokenAuthType tokenType) {
        this.tokenAuthRepo.deleteExpiredTokens(clock.instant(), tokenType);
    }

    @Override
    public Optional<TokenObject> findToken(String tokenValue) {
        Optional<TokenAuth> token = this.tokenAuthRepo.getTokenByValue(tokenValue);
        return token.isPresent()?Optional.of((TokenObject) token.get()):Optional.empty();
    }

    @Override
    public Stream<TokenObject> getAllTokensByEmail(String email) {
        return this.tokenAuthRepo.getTokensByEmail(email).stream()
            .map(elem ->  elem); //redundant casting(?), but required mapping
    }

    @Override
    public Stream<TokenObject> getAllExpiredTokens() {
        return this.tokenAuthRepo.getExpiredTokens(clock.instant())
            .stream().map(elem -> elem);
    }

    @Override
    public Stream<TokenObject> getAllExpiredTokens(String email) {
        return this.tokenAuthRepo.getTokensByEmail(email)
            .stream().filter(e -> e.getExpiredDateTime().isAfter(clock.instant()))
            .map(elem -> elem);
    }

    @Override
    public boolean present(TokenObject tokenObject) {
        return this.tokenAuthRepo.getTokenByEmailAndValueAndType(
            tokenObject.getEmail(),
            tokenObject.getTokenValue(),
            tokenObject.getTokenType()
        ).isPresent();
    }
}
