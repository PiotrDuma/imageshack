package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
  private static final String USER_EMAIL = "user@email.com";
  private static final String TOKEN_VALUE = "z3v2";
  private static final String APP_NAME = "imageshack";
  @Mock
  private UserService userService;
  @Mock
  private TokenAuthFacade tokenFacade;
  @Mock
  private AuthTokenSender authTokenSender;
  @Mock
  private Validator validator;
  private RegistrationService service;

  @BeforeEach
  void setUp(){
    this.service = new RegistrationServiceImpl(userService, tokenFacade,
         authTokenSender, validator);
  }

  @Test
  void registerShouldThrowWhenEmailExists(){
    String username = "username";
    String password = "password";
    AppUserDTO dto = new AppUserDTO(username, USER_EMAIL, password);

    when(this.userService.existsByEmail(any())).thenReturn(true);

    RegisterIOException result = assertThrows(RegisterIOException.class,
        () -> this.service.register(dto));
    assertTrue(result.isEmailTaken());
    verify(this.userService, times(0)).createNewUser(any(), any(), any());
  }

  @Test
  void registerShouldThrowWhenUsernameExists(){
    String username = "username";
    String password = "password";
    AppUserDTO dto = new AppUserDTO(username, USER_EMAIL, password);

    when(this.userService.existsByUsername(any())).thenReturn(true);

    RegisterIOException result = assertThrows(RegisterIOException.class,
        () -> this.service.register(dto));
    assertTrue(result.isLoginTaken());
    verify(this.userService, times(0)).createNewUser(any(), any(), any());
  }

  @Test
  void registerShouldCreateUser() throws RegisterIOException {
    String username = "username";
    String password = "password";
    AppUserDTO dto = new AppUserDTO(username, USER_EMAIL, password);

    this.service.register(dto);
    verify(this.userService, times(1)).createNewUser(username, USER_EMAIL, password);
  }

  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenEmailIsInvalid(){
    when(this.validator.validate(any())).thenReturn(false);

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenAccountNotFound(){
    when(this.validator.validate(anyString())).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(anyString())).thenReturn(Optional.empty());

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenAccountIsEnabled(){
    UserDetailsWrapper user = getValidatedWrapper();

    when(user.isEnabled()).thenReturn(true);

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenTokenNotFound(){
    Supplier<Stream<TokenObject>> supplier = Stream::empty;
    UserDetailsWrapper user = getValidatedWrapper();

    when(user.isEnabled()).thenReturn(false);
    when(this.tokenFacade.findByEmail(any())).thenReturn(supplier.get());

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenNoneTokenHasValidTokenValue(){
    TokenObject token = mock(TokenObject.class);
    Supplier<Stream<TokenObject>> supplier = () -> Stream.of(token);
    UserDetailsWrapper user = getValidatedWrapper();

    when(token.getTokenValue()).thenReturn("12311");
    when(user.isEnabled()).thenReturn(false);
    when(this.tokenFacade.findByEmail(any())).thenReturn(supplier.get());

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenNoneTokenHasValidTokenType(){
    TokenObject token = mock(TokenObject.class);
    Supplier<Stream<TokenObject>> supplier = () -> Stream.of(token);
    UserDetailsWrapper user = getValidatedWrapper();

    when(token.getTokenValue()).thenReturn(TOKEN_VALUE);
    when(token.getTokenType()).thenReturn(TokenAuthType.PASSWORD_RESET);
    when(user.isEnabled()).thenReturn(false);
    when(this.tokenFacade.findByEmail(any())).thenReturn(supplier.get());

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }


  @Test
  void authenticateShouldThrowRegistrationAuthExceptionWhenTokenIsInvalid(){
    TokenObject token = mock(TokenObject.class);
    Supplier<Stream<TokenObject>> supplier = () -> Stream.of(token);
    UserDetailsWrapper user = getValidatedWrapper();

    when(token.getTokenValue()).thenReturn(TOKEN_VALUE);
    when(token.getTokenType()).thenReturn(TokenAuthType.ACCOUNT_CONFIRMATION);
    when(user.isEnabled()).thenReturn(false);
    when(this.tokenFacade.findByEmail(any())).thenReturn(supplier.get());
    when(this.tokenFacade.isValid(token)).thenReturn(false);

    Exception result = assertThrows(RegistrationAuthException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldSetEnabledUserWhenTokenIsValid(){
    TokenObject token = mock(TokenObject.class);
    Supplier<Stream<TokenObject>> supplier = () -> Stream.of(token);
    UserDetailsWrapper user = getValidatedWrapper();

    when(token.getTokenValue()).thenReturn(TOKEN_VALUE);
    when(token.getTokenType()).thenReturn(TokenAuthType.ACCOUNT_CONFIRMATION);
    when(user.isEnabled()).thenReturn(false);
    when(this.tokenFacade.findByEmail(any())).thenReturn(supplier.get());
    when(this.tokenFacade.isValid(token)).thenReturn(true);

    this.service.authenticate(USER_EMAIL, TOKEN_VALUE);
    verify(user, times(1)).setEnabled(true);
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowRegistrationAuthExceptionWhenAccountIsEnabled(){
    String expected = "Account has been authenticated.";
    UserDetailsWrapper user = getValidatedWrapper();
    when(user.isEnabled()).thenReturn(true);

    Exception result = assertThrows(RegistrationAuthException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));

//    assertEquals(expected, result.getMessage());//TODO: refactor exception class
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowRegistrationAuthExceptionWhenEmailIsInvalid(){
    String expected = "Account with that email doesn't exists.";
    when(this.validator.validate(anyString())).thenReturn(false);

    Exception result = assertThrows(RegistrationAuthException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));

//    assertEquals(expected, result.getMessage());//TODO: refactor exception class
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowRegistrationAuthExceptionWhenAccountDoesntExists(){
    String expectedMessage = "Account with that email doesn't exists.";
    when(this.validator.validate(USER_EMAIL)).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(USER_EMAIL)).thenReturn(Optional.empty());

    Exception result = assertThrows(RegistrationAuthException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));

//    assertEquals(expectedMessage, result.getMessage());//TODO: refactor exception class
  }

  @Test
  void sendAccountAuthenticationTokenShouldPropagateExceptionWhenAuthTokenSenderThrows(){
    UserDetailsWrapper user = getValidatedWrapper();
    when(user.isEnabled()).thenReturn(false);

    doThrow(new RegistrationAuthException()).when(this.authTokenSender).send(any(), any());

    Exception result = assertThrows(RegistrationAuthException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));
  }

  private UserDetailsWrapper getValidatedWrapper(){
    UserDetailsWrapper user = mock(UserDetailsWrapper.class);
    when(this.validator.validate(anyString())).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(anyString())).thenReturn(Optional.of(user));
    return user;
  }
}