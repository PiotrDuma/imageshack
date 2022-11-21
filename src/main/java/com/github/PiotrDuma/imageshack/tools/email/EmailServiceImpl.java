package com.github.PiotrDuma.imageshack.tools.email;

import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("systemEmailService")
class EmailServiceImpl implements EmailService{
  private static final String ERROR_MESSAGE = "System couldn't send email.";
  private final JavaMailSender mailSender;
  private final SystemEmailServiceConfig config;
  @Qualifier("emailValidator")
  private final Validator emailValidator;

  @Autowired
  public EmailServiceImpl(SystemEmailServiceConfig config, Validator emailValidator) {
    this.config = config;
    this.mailSender = config.mailSender();
    this.emailValidator = emailValidator;
  }

  @Override
  public void sendMail(String to, String subject, String message, boolean isHTML) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    if(!this.emailValidator.validate(to)){
     throw new InvalidEmailAddressException(to);
    }
    try{
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(to);
      //from must be same email as in JavaMailSender bean initialized in config file
      helper.setFrom(new InternetAddress(config.getFrom()));
      helper.setSubject(subject);
      helper.setText(message, isHTML);
      mailSender.send(mimeMessage);
    }catch(Exception ex){
      throw new EmailSendingException(ERROR_MESSAGE + " " + ex.getMessage(), ex.getCause());
    }
  }
}
