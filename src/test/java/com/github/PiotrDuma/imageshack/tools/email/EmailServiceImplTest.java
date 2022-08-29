package com.github.PiotrDuma.imageshack.tools.email;

import static org.junit.jupiter.api.Assertions.*;

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
  private static final String TO = "test@imageshack.com";
  private static final String TITLE = "title";
  private static final String TEXT = "text";
  @Mock
  private SystemEmailServiceConfig config;
  @Mock
  private JavaMailSender mailSender;
  private EmailService emailService;


  @BeforeEach
  void setup(){
    Mockito.when(this.config.mailSender()).thenReturn(mailSender);
    this.emailService = new EmailServiceImpl(config);
  }

  @Test
  void sendMailShouldReturnFailWhenSendingThrowsException(){
    MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);

    //when
    Mockito.when(this.config.getFrom()).thenReturn("from@anywhere.com");
    Mockito.when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);
    Mockito.doThrow(new RuntimeException("")).when(this.mailSender).send(mimeMessage);

    //then
    boolean result = this.emailService.sendMail(TO, TITLE, TEXT,false);
    assertFalse(result);
  }

  @Test
  void sendMailShouldCallSendOnceAndReturnTrue(){
    MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);

    //when
    Mockito.when(this.config.getFrom()).thenReturn("from@anywhere.com");
    Mockito.when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);

    //then
    boolean result = this.emailService.sendMail(TO, TITLE, TEXT,false);
    Mockito.verify(this.mailSender, Mockito.times(1)).send(mimeMessage);
    assertTrue(result);
  }
}