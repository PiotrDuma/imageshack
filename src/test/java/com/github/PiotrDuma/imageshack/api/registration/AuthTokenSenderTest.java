package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
class AuthTokenSenderTest {
  private static final String EMAIL = "user@service.com";
  private static final String USERNAME = "username";
  private static final String TOKEN_VALUE = "123zxc321";
  private static final String APP_NAME = "imageshack";
  private static final String SYSTEM_URL = "http://localhost:8080";
  private static final String ENDPOINT = "/registration/auth";
  private static String CONFIRM_URL = SYSTEM_URL + ENDPOINT + "?email=" + EMAIL + "&token=" + TOKEN_VALUE;
  private static final Instant TIMESTAMP = LocalDateTime.of(2022, 11, 26,
      6, 43, 12).toInstant(ZoneOffset.UTC);

  @Mock
  private EmailService emailService;
  @Mock
  private TokenAuthFacade tokenFacade;
  private AuthTokenSender service;

  @BeforeEach
  void setUp(){
    this.service = new AuthTokenSender(emailService, tokenFacade);
    ReflectionTestUtils.setField(this.service, "applicationName", APP_NAME);
    ReflectionTestUtils.setField(this.service, "systemUrl", SYSTEM_URL);
    ReflectionTestUtils.setField(this.service, "endpoint", ENDPOINT);
  }

  @Test
  void verifySendMethodAndParameters(){
    ArgumentCaptor<String> recipient = ArgumentCaptor.forClass(String.class);

    mockTokenObject();

    this.service.send(EMAIL, USERNAME);
    verify(tokenFacade, times(1)).create(any(TokenAuthDTO.class));
    verify(emailService, times(1))
        .sendMail(recipient.capture(), anyString(), anyString(), eq(false));

    assertEquals(EMAIL, recipient.getValue());
  }

  @Test
  void throwExceptionWhenEmailServiceThrowsSendingException(){
    String message = "Email sending has failed.";
    mockTokenObject();
    doThrow(new EmailSendingException("")).when(this.emailService)
        .sendMail(anyString(), anyString(), anyString(), anyBoolean());

    Exception ex = assertThrows(RegistrationAuthProcessingException.class,
        () -> this.service.send(EMAIL, USERNAME));
    assertEquals(message, ex.getMessage());
  }

  @Test
  void throwExceptionWhenEmailServiceThrowsInvalidEmailAddressException(){
    String message = "Invalid email address. Email sending has failed.";
    mockTokenObject();
    doThrow(new InvalidEmailAddressException("")).when(this.emailService)
        .sendMail(anyString(), anyString(), anyString(), anyBoolean());

    Exception ex = assertThrows(RegistrationAuthProcessingException.class,
        () -> this.service.send(EMAIL, USERNAME));

    assertEquals(message, ex.getMessage());
  }

  @Test
  void throwExceptionWhenTokenFacadeThrowsRuntimeException(){
    String message = "Token initialization has failed.";
    doThrow(new RuntimeException()).when(this.tokenFacade)
        .create(any(TokenAuthDTO.class));

    Exception ex = assertThrows(RegistrationAuthProcessingException.class,
        () -> this.service.send(EMAIL, USERNAME));

    assertEquals(message, ex.getMessage());
  }

  @Test
  void verifyTokenInitialization(){
    ArgumentCaptor<TokenAuthDTO> captor = ArgumentCaptor.forClass(TokenAuthDTO.class);

    mockTokenObject();
    this.service.send(EMAIL, USERNAME);
    verify(this.tokenFacade, times(1)).create(captor.capture());

    assertEquals(EMAIL, captor.getValue().getEmail());
    assertEquals(TokenAuthType.ACCOUNT_CONFIRMATION, captor.getValue().getTokenType());
  }

  @Test
  void verifyNonHTMLEmailPayload(){
    String expectedSubject = APP_NAME + ": account activation";
    String message = "Hello!\n\nYou have created an account in %s website.\n"
        + "Login: %s / %s\n\n"
        + "If you want to activate your profile please click in link below:\n\n"
        + "%s\n\n"
        + "If you didn't register please ignore this email. The profile will be deleted soon.\n"
        + "Activation time expires at: %s\n\n"
        + "This email has been automatically generated. Please do not reply.";
    String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss")
        .withZone(ZoneOffset.systemDefault()).format(TIMESTAMP);
    String expectedMessage = String.format(message, APP_NAME, EMAIL, USERNAME, CONFIRM_URL, datetime);
    ArgumentCaptor<String> messageResult = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> subjectResult = ArgumentCaptor.forClass(String.class);

    mockTokenObject();
    this.service.send(EMAIL, USERNAME);
    verify(emailService).sendMail(anyString(), subjectResult.capture(), messageResult.capture(), eq(false));

    assertEquals(expectedSubject, subjectResult.getValue());
    assertEquals(expectedMessage, messageResult.getValue());
  }

  private void mockTokenObject(){
    TokenObject tokenObject = mock(TokenObject.class);
    when(tokenObject.getTokenValue()).thenReturn(TOKEN_VALUE);
    when(this.tokenFacade.create(any())).thenReturn(tokenObject);
    when(this.tokenFacade.expiresAt(tokenObject)).thenReturn(TIMESTAMP);
  }
}