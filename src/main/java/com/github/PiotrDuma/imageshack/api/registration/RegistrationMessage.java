package com.github.PiotrDuma.imageshack.api.registration;

import java.time.Instant;

interface RegistrationMessage {
  String generate(String email, String login, String tokenValue, Instant tokenExpiresAt, boolean isHtml);
}
