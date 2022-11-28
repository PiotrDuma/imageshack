package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAccountEnabledException.RegistrationAccountEnabledException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailAddressException.RegistrationEmailAddressException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
  private static final String USER_EMAIL = "user@email.com";
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
    String message = "message";
    String subject = APP_NAME + ": account activation";

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
    UserDetailsWrapper user = getValidatedWrapper();
    when(user.isEnabled()).thenReturn(false);
    when(this.registrationMessage.generate(anyString(),anyString(), anyString(),
        any(Instant.class), anyBoolean())).thenReturn("message");
    doThrow(EmailSendingException.class).when(this.emailService).sendMail(anyString(), anyString(),
        anyString(), anyBoolean());

    Exception result = assertThrows(RegistrationEmailSendingException.class, () ->
        this.service.sendAccountAuthenticationToken(anyString()));
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

  private UserDetailsWrapper getValidatedWrapper(){
    UserDetailsWrapper user = mock(UserDetailsWrapper.class);
    when(this.validator.validate(USER_EMAIL)).thenReturn(true);
    when(this.userService.loadUserWrapperByUsername(USER_EMAIL)).thenReturn(Optional.of(user));
    return user;
  }
}