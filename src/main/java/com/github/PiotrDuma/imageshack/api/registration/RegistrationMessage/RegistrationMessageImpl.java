package com.github.PiotrDuma.imageshack.api.registration.RegistrationMessage;

import org.springframework.stereotype.Component;

@Component("registrationMessage")
class RegistrationMessageImpl implements RegistrationMessage{
  @Override
  public String generate(String email, String tokenValue, boolean isHtml) {
    return null;
  }
}
