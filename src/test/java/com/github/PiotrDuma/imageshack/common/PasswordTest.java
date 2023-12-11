package com.github.PiotrDuma.imageshack.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  @Test
  void shouldPassWhenPasswordIsValid() {
    Password password = new Password("Password1");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);

    assertTrue(validation.isEmpty());
  }

  @Test
  void shouldPassWhenPasswordContainsSpecialSigns() {
    Password password = new Password("Po1!@#$%^&*()<>,.{}:");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);

    assertTrue(validation.isEmpty());
  }

  @Test
  void shouldFailValidationWhenPasswordHasNoDigit() {
    String expectedMessage = "Password must contain at least one digit";
    Password password = new Password("Password");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordHasNoUpperCase() {
    String expectedMessage = "Password must contain at least one uppercase character";
    Password password = new Password("password123");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordHasNoLowerCase() {
    String expectedMessage = "Password must contain at least one lowercase character";
    Password password = new Password("PASSWORD123");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordHasBlankSigns() {
    String expectedMessage = "Password cannot contain blank characters";
    Password password = new Password(" ");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsBlank() {
    String expectedMessage = "Password cannot be blank";
    Password password = new Password("");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsNull() {
    String expectedMessage = "Password cannot be blank";
    Password password = new Password(null);

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsTooLong() {
    String expectedMessage = "Password must contain max 32 characters";
    Password password = new Password(new RandomString(33).nextString());

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsTooShort() {
    String expectedMessage = "Password must contain at least 8 characters";
    Password password = new Password("1234567");

    Set<ConstraintViolation<Password>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }
}