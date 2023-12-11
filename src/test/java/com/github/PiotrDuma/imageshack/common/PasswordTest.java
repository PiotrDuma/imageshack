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
    String password = "Password1";
    Set<ConstraintViolation<String>> validation = validator.validate(password);

    assertTrue(validation.isEmpty());
  }

  @Test
  void shouldPassWhenPasswordContainsSpecialSigns() {
    String password = "Po1!@#$%^&*()<>,.{}:";

    Set<ConstraintViolation<String>> validation = validator.validate(password);

    assertTrue(validation.isEmpty());
  }

  @Test
  void returnFalseWhenPasswordHasNoDigit() {
    String expectedMessage = "Password must contain at least one digit";
    String password = "Password";

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void returnFalseWhenPasswordHasNoUpperCase() {
    String expectedMessage = "Password must contain at least one uppercase character";
    String password = "password123";

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void returnFalseWhenPasswordHasNoLowerCase() {
    String expectedMessage = "Password must contain at least one lowercase character";
    String password = "PASSWORD123";

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void returnFalseWhenPasswordHasBlankSigns() {
    String expectedMessage = "Password cannot contain blank characters";
    String password = " ";

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsBlank() {
    String expectedMessage = "Password cannot be blank";
    String password = "";

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsNull() {
    String expectedMessage = "Password cannot be blank";
    String password = null;

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsTooLong() {
    String expectedMessage = "Password must contain max 32 characters";
    String password = new RandomString(33).nextString();

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenPasswordIsTooShort() {
    String expectedMessage = "Password must contain at least 8 characters";
    String password = "12345";

    Set<ConstraintViolation<String>> validation = validator.validate(password);
    Set<String> result = validation.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }
}