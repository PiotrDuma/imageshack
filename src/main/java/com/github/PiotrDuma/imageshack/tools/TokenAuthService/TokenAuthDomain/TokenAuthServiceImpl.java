package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthService;
import com.github.PiotrDuma.imageshack.tools.TokenGenerator.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

class TokenAuthServiceImpl implements TokenAuthService {
    private static final String NOT_FOUND_BY_EMAIL = "TOKEN NOT FOUND BY EMAIL: %s";
    private final TokenAuthRepo tokenAuthRepo;
    private final TokenGenerator tokenGenerator;

    @Autowired
    public TokenAuthServiceImpl(TokenAuthRepo tokenAuthRepo, TokenGenerator tokenGenerator) {
        this.tokenAuthRepo = tokenAuthRepo;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public TokenAuthDTO createToken(String email, TokenAuthType tokenAuthType, int activeTimeInMinutes) {
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime expired = created.plusMinutes(30);
        TokenAuth tokenAuth = new TokenAuth(email, tokenGenerator.generate(), tokenAuthType,
                created, expired);
        this.tokenAuthRepo.save(tokenAuth);
        return new TokenAuthDTO(tokenAuth);
    }

    @Override
    public boolean isTokenActive(String email, String token, TokenAuthType tokenAuthType)
                                    throws TokenAuthNotFoundException {
        TokenAuth tokenAuth = tokenAuthRepo.getTokenByEmail(email)
                .orElseThrow(() -> new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL, email)));

        return tokenAuth.getToken().equals(token)&&tokenAuth.getTokenAuthType().equals(tokenAuthType)?true:false;
    }

    @Override
    @Transactional
    public void deleteToken(String email, TokenAuthType tokenAuthType) {
        List<TokenAuth> tokens = this.tokenAuthRepo.getTokensByEmail(email);
        tokens.stream().filter(e -> e.getTokenAuthType().equals(tokenAuthType))
                .forEach(k -> this.tokenAuthRepo.delete(k));
    }

    @Override
    public TokenAuthDTO findToken(String email, TokenAuthType tokenAuthType) {
        TokenAuth token = this.tokenAuthRepo.getTokenByEmail(email)
                .orElseThrow(() ->new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL, email)));
        return new TokenAuthDTO(token);
    }

    @Override
    public Stream<TokenAuthDTO> getAllTokensByEmail(String email) {
        return this.tokenAuthRepo.getTokensByEmail(email).stream().map(e -> new TokenAuthDTO((e)));
    }

    @Override //TODO
    public Stream<TokenAuthDTO> getAllExpiredTokens() {
        return null;
    }
}
