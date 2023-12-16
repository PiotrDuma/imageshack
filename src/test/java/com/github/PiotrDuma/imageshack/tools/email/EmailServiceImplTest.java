package com.github.PiotrDuma.imageshack.tools.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.PiotrDuma.imageshack.common.EmailAddress;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
@ExtendWith(OutputCaptureExtension.class)
class EmailServiceImplTest {
  private static final String INTERNAL_EXCEPTION = "Error thrown during email service configuration: ";
  private static final String EXTERNAL_EXCEPTION = "External service thrown email sending exception: ";
  private static final String TO = "test@imageshack.com";
  private static final String TITLE = "title";
  private static final String TEXT = "text";
  private static final boolean IS_HTML = false;
  @Mock
  private SystemEmailServiceConfig config;
  @Mock
  private JavaMailSender mailSender;
  private EmailService emailService;
  private MimeMessage mimeMessage;

  private AbstractEmailContentProvider emailContentProvider;

  @BeforeEach
  void setup(){
    emailContentProvider = new ExampleEmailContentProvider(TITLE,   TEXT, IS_HTML);
    mimeMessage = new MimeMessage((Session) null);
    when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);
    when(this.config.mailSender()).thenReturn(mailSender);
    this.emailService = new EmailServiceImpl(config);
  }

  @Test
  void sendMailShouldLogExceptionWhenExternalEmailServiceThrowsMailAuthenticationException(CapturedOutput output){
    String exceptionMessage = "ex message";
    EmailAddress sendTo = new EmailAddress(TO);

    when(this.config.getFrom()).thenReturn("from@anywhere.com");
    doThrow(new MailAuthenticationException(exceptionMessage))
        .when(this.mailSender).send(any(MimeMessage.class));

    this.emailService.sendMail(sendTo, emailContentProvider);
    verify(this.mailSender, Mockito.times(1)).send(mimeMessage);

    assertTrue(output.getOut().contains(EXTERNAL_EXCEPTION + exceptionMessage));
    assertTrue(output.getOut().contains("WARN"));
  }

  @Test
  void sendMailShouldLogExceptionWhenExternalEmailServiceThrowsMailSendException(CapturedOutput output){
    String exceptionMessage = "ex message";
    EmailAddress sendTo = new EmailAddress(TO);

    when(this.config.getFrom()).thenReturn("from@anywhere.com");
    doThrow(new MailSendException(exceptionMessage))
        .when(this.mailSender).send(any(MimeMessage.class));

    this.emailService.sendMail(sendTo, emailContentProvider);
    verify(this.mailSender, Mockito.times(1)).send(mimeMessage);

    assertTrue(output.getOut().contains(EXTERNAL_EXCEPTION + exceptionMessage));
    assertTrue(output.getOut().contains("WARN"));
  }

  @Test
  void sendMailShouldLogExceptionWhenSystemEmailIsNull(CapturedOutput output){
    String exceptionMessage = "Provided null parameter value";
    EmailAddress sendTo = new EmailAddress(TO);

    when(this.config.getFrom()).thenReturn(null);

    this.emailService.sendMail(sendTo, emailContentProvider);

    verify(this.mailSender, Mockito.times(0)).send(mimeMessage);

    assertTrue(output.getOut().contains(INTERNAL_EXCEPTION + exceptionMessage));
    assertTrue(output.getOut().contains("ERROR"));
  }

  @Test
  void sendMailShouldLogExceptionWhenSystemEmailIsEmpty(CapturedOutput output){
    EmailAddress sendTo = new EmailAddress(TO);

    when(this.config.getFrom()).thenReturn("");

    this.emailService.sendMail(sendTo, emailContentProvider);

    verify(this.mailSender, Mockito.times(0)).send(mimeMessage);
    assertTrue(output.getOut().contains(INTERNAL_EXCEPTION));
    assertTrue(output.getOut().contains("ERROR"));
  }

  @Test
  void sendMailShouldCallSendOnceAndReturnTrue(CapturedOutput output) throws Exception{
    String logMessage = "Message sent to: " + TO;
    String from = "from@anywhere.com";
    EmailAddress sendTo = new EmailAddress(TO);
    //when
    when(this.config.getFrom()).thenReturn(from);

    //then
    this.emailService.sendMail(sendTo, emailContentProvider);
    verify(this.mailSender, Mockito.times(1)).send(mimeMessage);

    assertEquals(1, mimeMessage.getAllRecipients().length);
    assertEquals(TO, mimeMessage.getAllRecipients()[0].toString());
    assertEquals(from, mimeMessage.getFrom()[0].toString());
    assertEquals(TITLE, mimeMessage.getSubject());
    assertEquals(TEXT, mimeMessage.getContent().toString());
    assertTrue(output.getOut().contains(logMessage));
    assertTrue(output.getOut().contains("INFO"));
  }
}