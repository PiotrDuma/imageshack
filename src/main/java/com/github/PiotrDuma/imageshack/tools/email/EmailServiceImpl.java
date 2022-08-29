package com.github.PiotrDuma.imageshack.tools.email;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("systemEmailService")
public class EmailServiceImpl implements EmailService{
  private final JavaMailSender mailSender;
  private final SystemEmailServiceConfig config;

  @Autowired
  public EmailServiceImpl(SystemEmailServiceConfig config) {
    this.config = config;
    this.mailSender = config.mailSender();
  }

  @Override
  public boolean sendMail(String to, String subject, String message, boolean isHTML) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try{
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(to);
      //from must be same email as in JavaMailSender bean initialized in config file
      helper.setFrom(new InternetAddress(config.getFrom()));
      helper.setSubject(subject);
      helper.setText(message, isHTML);
      mailSender.send(mimeMessage);
    }catch(Exception ex){
      System.out.println(ex.getMessage());
      System.out.println("Something went wrong. Email cannot be sent.");
      return false;
    }
    return true;
  }

}
