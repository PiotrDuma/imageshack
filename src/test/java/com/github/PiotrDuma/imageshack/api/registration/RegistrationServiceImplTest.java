package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.NothingToAuthException.NothingToAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.api.registration.RegistrationMessage.RegistrationMessage;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
  private static final String NOTHING_TO_AUTHENTICATE = "Nothing to authenticate";
  private static final String USER_EMAIL = "user@email.com";
  @Mock
  private UserService userService;
  @Mock
  private EmailService emailService;
  @Mock
  private TokenAuthFacade tokenFacade;
  @Mock
  private RegistrationMessage registrationMessage;
  private RegistrationService service;

  @BeforeEach
  void setUp(){
    this.service = new RegistrationServiceImpl(userService, emailService, tokenFacade, registrationMessage);
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowExceptionWhenAccountNotExists(){
    when(this.userService.loadUserByUsername(USER_EMAIL)).thenThrow(UsernameNotFoundException.class);

    Exception result = assertThrows(NothingToAuthException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));

    assertEquals(NOTHING_TO_AUTHENTICATE, result.getMessage());
  }

  @Test
  void sendAccountAuthenticationTokenShouldThrowExceptionWhenAccountIsEnabled(){
    UserDetails user = mock(UserDetails.class);
    when(this.userService.loadUserByUsername(USER_EMAIL)).thenReturn(user);
    when(user.isEnabled()).thenReturn(true);

    Exception result = assertThrows(NothingToAuthException.class, () ->
        this.service.sendAccountAuthenticationToken(USER_EMAIL));

    assertEquals(NOTHING_TO_AUTHENTICATE, result.getMessage());
  }

  @Test
  void sendAccountAuthenticationTokenShouldSendSystemEmailWithValidParameters(){
    UserDetails user = mock(UserDetails.class);
    String message = "message";
    String subject = "subject";
    when(this.userService.loadUserByUsername(USER_EMAIL)).thenReturn(user);
    when(user.isEnabled()).thenReturn(true);
    when(this.registrationMessage.generate(anyString(),anyString(), anyBoolean()))
        .thenReturn(message);

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
    UserDetails user = mock(UserDetails.class);
    when(this.userService.loadUserByUsername(USER_EMAIL)).thenReturn(user);
    when(user.isEnabled()).thenReturn(true);
    when(this.registrationMessage.generate(anyString(),anyString(), anyBoolean()))
        .thenReturn("message");
    doThrow(EmailSendingException.class).when(this.emailService).sendMail(anyString(), anyString(),
        anyString(), anyBoolean());

    Exception result = assertThrows(RegistrationEmailSendingException.class, () ->
        this.service.sendAccountAuthenticationToken(anyString()));
  }
}