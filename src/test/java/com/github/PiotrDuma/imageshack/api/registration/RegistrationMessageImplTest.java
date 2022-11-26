package com.github.PiotrDuma.imageshack.api.registration;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class RegistrationMessageImplTest {
  private static final String EMAIL = "user@service.com";
  private static final String USERNAME = "username";
  private static final String TOKEN_VALUE = "123zxc321";
  private static final String APP_NAME = "imageshack";
  private static final String SYSTEM_URL = "http://localhost:8080";
  private static final String ENDPOINT = "/registration/auth";
  private static String CONFIRM_URL = SYSTEM_URL + ENDPOINT + "?email=" + EMAIL + "&token=" + TOKEN_VALUE;
  private static final Instant TIMESTAMP = LocalDateTime.of(2022, 11, 26,
      6, 43, 12).toInstant(ZoneOffset.UTC);

  private RegistrationMessage service;

  @BeforeEach
  void setUp(){
    this.service = new RegistrationMessageImpl();
    ReflectionTestUtils.setField(this.service, "applicationName", APP_NAME);
    ReflectionTestUtils.setField(this.service, "systemUrl", SYSTEM_URL);
    ReflectionTestUtils.setField(this.service, "endpoint", ENDPOINT);
  }

  @Test
  void generateShouldReturnNonHTMLMessage(){
    String message = "Hello!\n\nYou have created an account in %s website.\n"
        + "Login: %s / %s\n\n"
        + "If you want to activate your profile please click in link below:\n\n"
        + "%s\n\n"
        + "If you didn't register please ignore this email. The profile will be deleted soon.\n"
        + "Activation time expires at: %s\n\n"
        + "This email has been automatically generated. Please do not reply.";
    String datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss")
        .withZone(ZoneOffset.systemDefault()).format(TIMESTAMP);
    String expected = String.format(message, APP_NAME, EMAIL, USERNAME, CONFIRM_URL, datetime);

    String result = this.service.generate(EMAIL, USERNAME, TOKEN_VALUE, TIMESTAMP, false);

    assertEquals(expected, result);
  }

  @Test
  void generateShouldReturnHTMLMessage(){
    //TODO:
    String result = this.service.generate(EMAIL, USERNAME, TOKEN_VALUE, TIMESTAMP, true);
  }
}