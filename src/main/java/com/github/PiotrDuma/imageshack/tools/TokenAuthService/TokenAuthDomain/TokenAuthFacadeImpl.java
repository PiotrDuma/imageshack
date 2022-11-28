package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.EmailValidator;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import java.time.Instant;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class TokenAuthFacadeImpl implements TokenAuthFacade {
  private final static String MESSAGE_NULL_TYPE = "TokenAuth type cannot be null.";
  private final static String MESSAGE_NULL_EMAIL = "Token email cannot be null.";
  private final static String TOKEN_NOT_FOUND = "Token with email: %s and value: %s has not been found.";
  private final TokenAuthService service;
  private final EmailValidator emailValidator;

  @Autowired
  public TokenAuthFacadeImpl(TokenAuthService service, EmailValidator emailValidator) {
    this.service = service;
    this.emailValidator = emailValidator;
  }

  @Override
  public TokenObject create(TokenAuthDTO tokenObject) {
    checkInput(tokenObject);
    if(!this.emailValidator.validate(tokenObject.getEmail())){
      throw new InvalidEmailAddressException(tokenObject.getEmail());
    }
    return this.service.createToken(tokenObject);
  }

  @Override
  public boolean isValid(TokenObject tokenObject) throws RuntimeException {
    isTokenExists(tokenObject);
    return this.service.isActive(tokenObject);
  }

  @Override
  @Transactional
  public void delete(TokenObject tokenObject) throws RuntimeException {
    isTokenExists(tokenObject);
    this.service.delete(tokenObject);
  }

  @Override
  public Stream<TokenObject> find(String tokenValue) {
    return this.service.findToken(tokenValue).stream();
  }

  @Override
  public Stream<TokenObject> findByEmail(String email) {
    return this.service.getAllTokensByEmail(email);
  }

  @Override
  @Transactional
  public void deleteExpiredTokens(String email) {
    this.service.getAllExpiredTokens(email)
        .forEach(token -> this.service.delete(token));
  }

  private void isTokenExists(TokenObject token){
    if(!this.service.present(token)){
      throw new RuntimeException(String.format(TOKEN_NOT_FOUND, token.getEmail(), token.getTokenValue()));
    }
  }

  private static void checkInput(@NotNull TokenAuthDTO token){
    if(token.getTokenType() == null){
      throw new RuntimeException(MESSAGE_NULL_TYPE);
    }
    String email = token.getEmail();
    if(email == null){
      throw new RuntimeException(MESSAGE_NULL_EMAIL);
    }
  }

  @Override
  public Instant expiresAt(TokenObject tokenObject) throws RuntimeException{
    return this.service.expiresAt(tokenObject);
  }
}
