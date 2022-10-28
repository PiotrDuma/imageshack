package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthNotFoundException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenGenerator.TokenGenerator;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;
import org.springframework.stereotype.Service;

//TODO: REFACTORING, ADD TESTS
@Service
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
    public TokenObject createToken(TokenAuthDTO tokenAuthDTO) {
        return null;
    }

    @Override
    public boolean isActive(TokenObject tokenObject) {
        return false;
    }

    @Override
    public Instant expiresAt(TokenObject tokenObject) throws TokenAuthNotFoundException {
        return null;
    }

    @Override
    public void delete(TokenObject tokenObject) throws TokenAuthNotFoundException {

    }

    @Override
    public void delete(String email) throws TokenAuthNotFoundException {

    }

    @Override
    public void delete(String tokenValue, TokenAuthType tokenType)
        throws TokenAuthNotFoundException {

    }

    @Override
    public void deleteExpired() {

    }

    @Override
    public void deleteExpired(String email) {

    }

    @Override
    public void deleteExpired(TokenAuthType tokenType) {

    }

    @Override
    public Optional<TokenObject> findToken(String tokenValue) {
        return Optional.empty();
    }

    @Override
    public Stream<TokenObject> getAllTokensByEmail(String email) {
        return null;
    }

    @Override
    public Stream<TokenObject> getAllExpiredTokens() {
        return null;
    }

    @Override
    public Stream<TokenObject> getAllExpiredTokens(String email) {
        return null;
    }

    @Override
    public boolean present(TokenObject tokenObject) {
        return false;
    }

    //    @Override
//    public TokenAuthDTO createToken(String email, TokenAuthType tokenAuthType, int activeTimeInMinutes) {
//        LocalDateTime created = LocalDateTime.now();
//        LocalDateTime expired = created.plusMinutes(30);
//        TokenAuth tokenAuth = new TokenAuth(email, tokenGenerator.generate(), tokenAuthType,
//                created, expired);
//        this.tokenAuthRepo.save(tokenAuth);
//        return new TokenAuthDTO(tokenAuth);
//    }

//    @Override
//    public boolean isTokenActive(String email, String token, TokenAuthType tokenAuthType)
//                                    throws TokenAuthNotFoundException {
//        TokenAuth tokenAuth = tokenAuthRepo.getTokenByEmail(email)
//                .orElseThrow(() -> new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL, email)));
//
//        return tokenAuth.getToken().equals(token)&&tokenAuth.getTokenAuthType().equals(tokenAuthType)?true:false;
//    }

//    @Override
//    public <T extends AbstractTokenObject> T createToken(String email, T tokenObject) {
//        return null;
//    }
//
//    @Override
//    public boolean isActive(String email, String token, TokenAuthType tokenAuthType) {
//        return false;
//    }
//
//    @Override
//    @Transactional
//    public void deleteToken(String email, TokenAuthType tokenAuthType) {
//        List<TokenAuth> tokens = this.tokenAuthRepo.getTokensByEmail(email);
//        tokens.stream().filter(e -> e.getTokenAuthType().equals(tokenAuthType))
//                .forEach(k -> this.tokenAuthRepo.delete(k));
//    }
//
//    @Override
//    public <T extends AbstractTokenObject> T findToken(String tokenValue) {
//        return null;
//    }
//
////    @Override
////    public TokenAuthDTO findToken(String email, TokenAuthType tokenAuthType) {
////        TokenAuth token = this.tokenAuthRepo.getTokenByEmail(email)
////                .orElseThrow(() ->new TokenAuthNotFoundException(String.format(NOT_FOUND_BY_EMAIL, email)));
////        return new TokenAuthDTO(token);
////    }
//
//    @Override
//    public Stream<TokenAuthDTO> getAllTokensByEmail(String email) {
//        return this.tokenAuthRepo.getTokensByEmail(email).stream().map(e -> new TokenAuthDTO((e)));
//    }
//
//    @Override
//    public Stream<TokenAuthDTO> getAllExpiredTokens() {
//        return this.tokenAuthRepo.getExpiredTokens(Instant.now())
//            .map(elem -> new TokenAuthDTO(elem));
//    }
}
