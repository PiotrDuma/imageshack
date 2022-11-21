package com.github.PiotrDuma.imageshack.tools.email;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
class SystemEmailServiceConfig {
  private static final String EMAIL_HOST = "smtp-mail.outlook.com";
  private static final int EMAIL_PORT = 587;
  private static final String EMAIL_USERNAME = "imageshack-system.message@outlook.com";
  private static final String EMAIL_PASSWORD = "Imageshackpass123";


  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(EMAIL_HOST);
    mailSender.setUsername(EMAIL_USERNAME);
    mailSender.setPassword(EMAIL_PASSWORD);
    mailSender.setPort(EMAIL_PORT);

    Properties properties = setProperties(mailSender.getJavaMailProperties());
    mailSender.setJavaMailProperties(properties);
    return mailSender;
  }

  private Properties setProperties(Properties properties){
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", Boolean.TRUE);
    properties.put("mail.smtp.starttls.required", Boolean.TRUE);
    properties.put("mail.smtp.starttls.enable", Boolean.TRUE);
    properties.put("mail.smtp.ssl.enable", Boolean.FALSE);
    properties.put("mail.test-connection", Boolean.TRUE);
    properties.put("mail.debug", Boolean.TRUE);
    return properties;
  }

  public String getFrom(){
    return EMAIL_USERNAME;
  }
}
