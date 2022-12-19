package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAccountEnabledException.RegistrationAccountEnabledException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailAddressException.RegistrationEmailAddressException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
  private static final String USER_EMAIL = "user@email.com";
  private static final String TOKEN_VALUE = "z3v2";
  private static final String APP_NAME = "imageshack";
  @Mock
  private UserService userService;
  @Mock
  private EmailService emailService;
  @Mock
  private TokenAuthFacade tokenFacade;
  @Mock
  private RegistrationMessage registrationMessage;
  @Mock
  private Validator validator;
  private RegistrationService service;

  @BeforeEach
  void setUp(){
    this.service = new RegistrationServiceImpl(userService, emailService, tokenFacade,
        registrationMessage, validator);
    ReflectionTestUtils.setField(this.service, "appName", APP_NAME);
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
  void authenticateShouldThrowRegistrationEmailAddressEcceptionWhenEmailIsInvalid(){
    when(this.validator.validate(any())).thenReturn(false);

    Exception result = assertThrows(RegistrationEmailAddressException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationExceptionWhenAccountNotFound(){
    when(this.validator.validate(anyString())).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(anyString())).thenReturn(Optional.empty());

    Exception result = assertThrows(RegistrationEmailAddressException.class,
        () -> this.service.authenticate(USER_EMAIL, TOKEN_VALUE));
  }

  @Test
  void authenticateShouldThrowRegistrationAccountEnabledExceptionWhenAccountIsEnabled(){
    UserDetailsWrapper user = getValidatedWrapper();

    when(user.isEnabled()).thenReturn(true);

    Exception result = assertThrows(RegistrationAccountEnabledException.class,
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
  void sendAccountAuthenticationTokenShouldThrowExceptionWhenAccountIsEnabled(){
    String expected = "Account has been authenticated.";
    UserDetailsWrapper user = getValidatedWrapper();
    when(user.isEnabled()).thenReturn(true);

    Exception result = assertThrows(RegistrationAccountEnabledException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));

    assertEquals(expected, result.getMessage());
  }

  @Test
  void sendAccountAuthenticationTokenShouldSendSystemEmailWithValidParameters(){
    UserDetailsWrapper user = getValidatedWrapper();
    TokenObject token = getCreatedTokenObject();
    String message = "message";
    String subject = APP_NAME + ": account activation";
    Instant timestamp = Instant.ofEpochSecond(12345);

    when(user.getUsername()).thenReturn("username");
    when(this.tokenFacade.expiresAt(token)).thenReturn(timestamp);
    when(user.isEnabled()).thenReturn(false);
    when(this.registrationMessage.generate(anyString(),anyString(), anyString(),
        any(Instant.class), anyBoolean())).thenReturn(message);

    ArgumentCaptor<String> addresseeResult = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> subjectResult = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageResult = ArgumentCaptor.forClass(String.class);

    this.service.sendAccountAuthenticationToken(USER_EMAIL);
    verify(this.emailService, times(1)).sendMail(
        addresseeResult.capture(),
        subjectResult.capture(),
        messageResult.capture(),
        anyBoolean()
    );

    assertEquals(USER_EMAIL, addresseeResult.getValue());
    assertEquals(subject, subjectResult.getValue());
    assertEquals(message, messageResult.getValue());
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowRegistrationEmailSendingExceptionWhenMailNotSent(){
    Instant timestamp = Instant.ofEpochSecond(12345);
    UserDetailsWrapper user = getValidatedWrapper();
    TokenObject token = getCreatedTokenObject();

    when(user.getUsername()).thenReturn("user");
    when(user.isEnabled()).thenReturn(false);
    when(this.tokenFacade.expiresAt(any(TokenObject.class))).thenReturn(timestamp);
    when(this.registrationMessage.generate(anyString(),anyString(), anyString(),
        any(Instant.class), anyBoolean())).thenReturn("message");
    doThrow(EmailSendingException.class).when(this.emailService).sendMail(anyString(), anyString(),
        anyString(), anyBoolean());

    Exception result = assertThrows(RegistrationEmailSendingException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowWhenEmailIsInvalid(){
    String expected = "Account with that email doesn't exists.";
    when(this.validator.validate(anyString())).thenReturn(false);

    Exception result = assertThrows(RegistrationEmailAddressException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));
    assertEquals(expected, result.getMessage());
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowWhenAccountDoesntExists(){
    String expectedMessage = "Account with that email doesn't exists.";
    when(this.validator.validate(USER_EMAIL)).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(USER_EMAIL)).thenReturn(Optional.empty());

    Exception result = assertThrows(RegistrationEmailAddressException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));
    assertEquals(expectedMessage, result.getMessage());
  }

  private TokenObject getCreatedTokenObject(){
    TokenObject token = mock(TokenObject.class);
    when(this.tokenFacade.create(any())).thenReturn(token);
    when(token.getTokenValue()).thenReturn("123");
    return token;
  }
  private UserDetailsWrapper getValidatedWrapper(){
    UserDetailsWrapper user = mock(UserDetailsWrapper.class);
    when(this.validator.validate(anyString())).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(anyString())).thenReturn(Optional.of(user));
    return user;
  }
}