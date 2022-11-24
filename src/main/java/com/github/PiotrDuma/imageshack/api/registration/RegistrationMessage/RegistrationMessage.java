package com.github.PiotrDuma.imageshack.api.registration.RegistrationMessage;

public interface RegistrationMessage {
  String generate(String email, String tokenValue, boolean isHtml);
}
