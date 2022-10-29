package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.EmailValidator;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenAuthFacadeImplTest {
  private final static String MESSAGE_NULL_TYPE = "TokenAuth type cannot be null.";
  private final static String MESSAGE_NULL_EMAIL = "Token email cannot be null.";
  private final static String TOKEN_NOT_FOUND = "Token with email: %s and value: %s has not been found.";
  private static final String EMAIL_DTO = "test@imageshack.com";
  private static final TokenAuthType TOKEN_TYPE = TokenAuthType.PASSWORD_RESET;
  @Mock
  private TokenAuthService tokenAuthService;
  @Mock
  private EmailValidator validator;
  private TokenAuthFacade facade;
  private TokenAuthDTO inputDTO;

  @BeforeEach
  void setUp(){
    this.facade = new TokenAuthFacadeImpl(tokenAuthService, validator);
    inputDTO = new TokenAuthDTO(EMAIL_DTO, TOKEN_TYPE);
  }

  @Test
  void createShouldThrowInvalidEmailExceptionWhenEmailIsInvalid(){
    String email = "email  @sth .net";
    inputDTO.setEmail(email);
    Mockito.when(validator.validate(email)).thenReturn(false);
    Exception result = assertThrows(InvalidEmailAddressException.class, ()->this.facade.create(inputDTO));
  }

  @Test
  void createShouldThrowNullExceptionWhenEmailIsNull(){
    inputDTO.setEmail(null);

    Exception result = assertThrows(RuntimeException.class, ()->this.facade.create(inputDTO));
    assertEquals(result.getMessage(), MESSAGE_NULL_EMAIL);
  }

  @Test
  void createShouldThrowNullExceptionWhenTokenTypeIsNull(){
    inputDTO.setTokenType(null);

    Exception result = assertThrows(RuntimeException.class, ()->this.facade.create(inputDTO));
    assertEquals(result.getMessage(), MESSAGE_NULL_TYPE);
  }

  @Test
  void createShouldReturnTokenObject(){
    TokenObject tokenObject = Mockito.mock(TokenObject.class);

    Mockito.when(validator.validate(Mockito.anyString())).thenReturn(true);
    Mockito.when(tokenAuthService.createToken(inputDTO)).thenReturn(tokenObject);

    TokenObject result = this.facade.create(inputDTO);
    Mockito.verify(tokenAuthService, Mockito.times(1)).createToken(inputDTO);
    assertEquals(result, tokenObject);
  }

  @Test
  void isValidShouldReturnBooleanResponse(){
    TokenObject input = Mockito.mock(TokenObject.class);
    Mockito.when(tokenAuthService.isActive(input)).thenReturn(false);
    Mockito.when(tokenAuthService.present(input)).thenReturn(true);

    boolean result = this.facade.isValid(input);

    Mockito.verify(tokenAuthService, Mockito.times(1)).present(input);
    Mockito.verify(tokenAuthService, Mockito.times(1)).isActive(input);
    assertEquals(result, false);
  }

  @Test
  void isValidShouldThrowExceptionWhenTokenNotExists(){
    String tokenValue = "1234vh";
    TokenObject input = Mockito.mock(TokenObject.class);
    Mockito.when(tokenAuthService.present(input)).thenReturn(false);
    Mockito.when(input.getEmail()).thenReturn(EMAIL_DTO);
    Mockito.when(input.getTokenValue()).thenReturn(tokenValue);

    Exception result = assertThrows(RuntimeException.class, () -> this.facade.isValid(input));
    Mockito.verify(tokenAuthService, Mockito.times(1)).present(input);
    Mockito.verify(tokenAuthService, Mockito.times(0)).isActive(input);
    assertEquals(result.getMessage(), String.format(TOKEN_NOT_FOUND, EMAIL_DTO, tokenValue));
  }

  @Test
  void deleteShouldInvokeWhenInputIsValid(){
    TokenObject input = Mockito.mock(TokenObject.class);
    Mockito.when(tokenAuthService.present(input)).thenReturn(true);
    Mockito.doNothing().when(tokenAuthService).delete(input);

    this.facade.delete(input);
    Mockito.verify(tokenAuthService, Mockito.times(1)).present(input);
    Mockito.verify(tokenAuthService, Mockito.times(1)).delete(input);
  }

  @Test
  void deleteShouldThrowExceptionWhenTokenNotExists(){
    String tokenValue = "1234vh";
    TokenObject input = Mockito.mock(TokenObject.class);
    Mockito.when(tokenAuthService.present(input)).thenReturn(false);
    Mockito.when(input.getEmail()).thenReturn(EMAIL_DTO);
    Mockito.when(input.getTokenValue()).thenReturn(tokenValue);

    Exception result = assertThrows(RuntimeException.class, () -> this.facade.delete(input));
    Mockito.verify(tokenAuthService, Mockito.times(1)).present(input);
    Mockito.verify(tokenAuthService, Mockito.times(0)).delete(input);
    assertEquals(result.getMessage(), String.format(TOKEN_NOT_FOUND, EMAIL_DTO, tokenValue));
  }

  @Test
  void findShouldInvokeServiceAndReturnStreamWithPresentObject(){
    String tokenValue = "1234vh";
    TokenObject tokenObject = Mockito.mock(TokenObject.class);
    Mockito.when(tokenObject.getTokenValue()).thenReturn(tokenValue);
    Mockito.when(tokenAuthService.findToken(tokenValue)).thenReturn(Optional.of(tokenObject));

    this.facade.find(tokenValue);
    Mockito.verify(tokenAuthService, Mockito.times(1)).findToken(tokenValue);

    Supplier<Stream<TokenObject>> result = () -> this.facade.find(tokenValue);
    assertTrue(result.get().findAny().isPresent());
    assertEquals(result.get().count(), 1);
    assertEquals(result.get().findAny().get().getTokenValue(), tokenValue);
  }
  @Test
  void findByEmailShouldInvokeServiceAndReturnStreamWithPresentObjects(){
    TokenObject tokenObject = Mockito.mock(TokenObject.class);
    TokenObject tokenObject1 = Mockito.mock(TokenObject.class);
    Mockito.when(tokenObject.getEmail()).thenReturn(EMAIL_DTO);
    Mockito.when(tokenObject1.getEmail()).thenReturn(EMAIL_DTO);

    Supplier<Stream<TokenObject>> result = () -> Stream.of(tokenObject, tokenObject1);
    Mockito.when(tokenAuthService.getAllTokensByEmail(EMAIL_DTO)).thenReturn(result.get());

    this.facade.findByEmail(EMAIL_DTO);
    Mockito.verify(tokenAuthService, Mockito.times(1)).getAllTokensByEmail(EMAIL_DTO);

    assertEquals(result.get().count(), 2);
    assertTrue(result.get().allMatch(token -> token.getEmail().equals(EMAIL_DTO)));
  }

  @Test
  void deleteExpiredTokensShouldReturnStreamAndInvokeDeleteMethod(){
    TokenObject tokenObject = Mockito.mock(TokenObject.class);
    TokenObject tokenObject1 = Mockito.mock(TokenObject.class);

    Supplier<Stream<TokenObject>> result = () -> Stream.of(tokenObject, tokenObject1);

    Mockito.doNothing().when(tokenAuthService).delete(Mockito.any(TokenObject.class));
    Mockito.when(tokenAuthService.getAllExpiredTokens(EMAIL_DTO)).thenReturn(result.get());

    this.facade.deleteExpiredTokens(EMAIL_DTO);
    Mockito.verify(tokenAuthService, Mockito.times(1)).getAllExpiredTokens(EMAIL_DTO);
    Mockito.verify(tokenAuthService, Mockito.times(1)).delete(tokenObject);
    Mockito.verify(tokenAuthService, Mockito.times(1)).delete(tokenObject1);
  }
}