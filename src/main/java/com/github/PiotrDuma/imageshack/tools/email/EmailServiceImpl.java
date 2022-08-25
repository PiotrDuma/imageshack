package com.github.PiotrDuma.imageshack.tools.email;

import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("systemEmailService")
public class EmailServiceImpl implements EmailService{
  private final JavaMailSender mailSender;

  @Autowired
  public EmailServiceImpl(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Override
  public boolean sendMail(String to, String subject, String message, boolean isHTML) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try{
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(message, isHTML);
      mailSender.send(mimeMessage);
    }catch(Exception ex){
      System.out.println("Something went wrong. Email cannot be sent.");
      return false;
    }
    return true;
  }
}
