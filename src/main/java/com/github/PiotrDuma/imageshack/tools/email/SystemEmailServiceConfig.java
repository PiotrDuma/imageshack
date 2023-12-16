package com.github.PiotrDuma.imageshack.tools.email;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
class SystemEmailServiceConfig {
  @Value("${spring.application.email.smtp_host}")
  private String emailHost;
  @Value("${spring.application.email.port}")
  private int emailPort;
  @Value("${spring.application.email.account_name}")
  private String emailAccount;
  @Value("${spring.application.email.password}")
  private String emailPassword;

  @Bean
  public JavaMailSender mailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(emailHost);
    mailSender.setUsername(emailAccount);
    mailSender.setPassword(emailPassword);
    mailSender.setPort(emailPort);

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
    return emailAccount;
  }
}
