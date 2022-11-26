package com.github.PiotrDuma.imageshack.api.registration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("registrationMessage")
class RegistrationMessageImpl implements RegistrationMessage {
  private static final String MESSAGE = "Hello!\n\nYou have created an account in %s website.\n"
      + "Login: %s / %s\n\n"
      + "If you want to activate your profile please click in link below:\n\n"
      + "%s\n\n"
      + "If you didn't register please ignore this email. The profile will be deleted soon.\n"
      + "Activation time expires at: %s\n\n"
      + "This email has been automatically generated. Please do not reply.";

  @Value("${spring.application.name}") private String applicationName;
  @Value("${services.systemURL}") private String systemUrl;
  @Value("${services.registrationAuth}") private String endpoint;

  @Override
  public String generate(String email, String login, String tokenValue,
      Instant tokenExpiresAt, boolean isHtml) {
    String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss")
        .withZone(ZoneId.systemDefault()).format(tokenExpiresAt);
    if(isHtml){
      return "empty"; //TODO
    }else{
      return String.format(MESSAGE, applicationName, email, login,
              getAuthURL(email, tokenValue), timestamp);
    }
  }

  private String getAuthURL(String email, String tokenValue){
    return new StringBuilder().append(systemUrl)
        .append(endpoint)
        .append("?email=")
        .append(email)
        .append("&token=")
        .append(tokenValue)
        .toString();
  }
}
