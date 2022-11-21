package com.github.PiotrDuma.imageshack.tools.email;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {
  private static final String INVALID_EMAIL = "Invalid email address: %s.";
  private static final String SENDING_ERROR_MESSAGE = "System couldn't send email.";
  private static final String TO = "test@imageshack.com";
  private static final String TITLE = "title";
  private static final String TEXT = "text";
  @Mock
  private Validator validator;
  @Mock
  private SystemEmailServiceConfig config;
  @Mock
  private JavaMailSender mailSender;
  private EmailService emailService;
  private MimeMessage mimeMessage;

  @BeforeEach
  void setup(){
    mimeMessage = new MimeMessage((Session) null);
    Mockito.when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);
    Mockito.when(this.config.mailSender()).thenReturn(mailSender);
    this.emailService = new EmailServiceImpl(config, validator);
  }

  @Test
  void sendMailShouldThrowInvalidEmailExceptionWhenEmailIsNotValid(){
    //when
    Mockito.when(this.validator.validate(Mockito.anyString())).thenReturn(false);

    Exception result = assertThrows(InvalidEmailAddressException.class, () -> this.emailService
        .sendMail(TO, TITLE, TEXT, false));


    assertEquals(String.format(INVALID_EMAIL, TO), result.getMessage());
  }

  @Test
  void sendMailShouldThrowEmailSendingExceptionWhenMailIsNotSent(){
    Throwable cause = Mockito.mock(Throwable.class);
    String receivedMsg = "Something went wrong,";
    //when
    Mockito.when(this.config.getFrom()).thenReturn("from@anywhere.com");
    Mockito.when(this.validator.validate(Mockito.anyString())).thenReturn(true);
    Mockito.doThrow(new RuntimeException(receivedMsg, cause)).when(this.mailSender).send(mimeMessage);

    //then
    Exception result = assertThrows(EmailSendingException.class,
        () -> this.emailService.sendMail(TO, TITLE, TEXT,false));

    assertEquals(SENDING_ERROR_MESSAGE + " " + receivedMsg, result.getMessage());
    assertEquals(cause, result.getCause());
  }

  @Test
  void sendMailShouldCallSendOnceAndReturnTrue() throws MessagingException, IOException {
    String from = "from@anywhere.com";
    //when
    Mockito.when(this.config.getFrom()).thenReturn(from);
    Mockito.when(this.validator.validate(Mockito.anyString())).thenReturn(true);

    //then
    this.emailService.sendMail(TO, TITLE, TEXT,false);
    Mockito.verify(this.mailSender, Mockito.times(1)).send(mimeMessage);

    assertEquals(1, mimeMessage.getAllRecipients().length);
    assertEquals(TO, mimeMessage.getAllRecipients()[0].toString());
    assertEquals(from, mimeMessage.getFrom()[0].toString());
    assertEquals(TITLE, mimeMessage.getSubject());
    assertEquals(TEXT, mimeMessage.getContent().toString());
  }
}