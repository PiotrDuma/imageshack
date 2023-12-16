package com.github.PiotrDuma.imageshack.tools.email;

import com.github.PiotrDuma.imageshack.common.EmailAddress;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("systemEmailService")
class EmailServiceImpl implements EmailService{
  private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
  private static final String INTERNAL_EXCEPTION = "Error thrown during email service configuration: ";
  private static final String EXTERNAL_EXCEPTION = "External service thrown email sending exception: ";
  private static final String MESSAGE_SENT = "Message sent to: ";

  private final JavaMailSender mailSender;
  private final SystemEmailServiceConfig config;

  @Autowired
  public EmailServiceImpl(SystemEmailServiceConfig config) {
    this.config = config;
    this.mailSender = config.mailSender();
  }

  @Override
  public void sendMail(EmailAddress to, AbstractEmailContentProvider emailContentProvider) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try{
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(to.getEmail());
      //from must be same email as in JavaMailSender bean initialized in config file
      helper.setFrom(new InternetAddress(config.getFrom()));
      helper.setSubject(emailContentProvider.getSubject());
      helper.setText(emailContentProvider.getMessage(), emailContentProvider.isHTML());
      mailSender.send(mimeMessage);
      log.info(MESSAGE_SENT + to.getEmail());
    }catch(MessagingException ex) {
      log.error(INTERNAL_EXCEPTION + ex.getMessage(), ex.getCause());
    }catch(MailException ex){
      log.warn(EXTERNAL_EXCEPTION + ex.getMessage(), ex.getCause());
    }catch(NullPointerException ex) {
      log.error(INTERNAL_EXCEPTION + "Provided null parameter value", ex.getCause());
    }
  }
}
