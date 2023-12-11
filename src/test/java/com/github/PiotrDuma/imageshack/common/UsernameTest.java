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

class UsernameTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    this.validator = validatorFactory.getValidator();
  }

  @Test
  void shouldPassWhenUsernameIsValid(){
    Username username = new Username("ao123");

    Set<ConstraintViolation<Username>> validate = validator.validate(username);

    assertTrue(validate.isEmpty());
  }

  @Test
  void shouldPassWhenUsernameIsDigitsOnly(){
    Username username = new Username("123456");

    Set<ConstraintViolation<Username>> validate = validator.validate(username);

    assertTrue(validate.isEmpty());
  }

  @Test
  void shouldPassWhenUsernameContainsRestrictedSpecialCharacters(){
    Username username = new Username("ao.1_2-3");

    Set<ConstraintViolation<Username>> validate = validator.validate(username);

    assertTrue(validate.isEmpty());
  }

  @Test
  void shouldFailValidationWhenUsernameIsTooShort(){
    String expectedMessage = "Username must have at least 3 characters";
    Username username = new Username("ao");

    Set<ConstraintViolation<Username>> validate = validator.validate(username);
    Set<String> result = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenUsernameIsTooLong(){
    String expectedMessage = "Username must have less than 32 characters";
    Username username = new Username(new RandomString(33).nextString());

    Set<ConstraintViolation<Username>> validate = validator.validate(username);
    Set<String> result = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenUsernameContainsBlankCharacter(){
    String expectedMessage = "Username cannot be blank";
    Username username = new Username(" ");

    Set<ConstraintViolation<Username>> validate = validator.validate(username);
    Set<String> result = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenUsernameIsNull(){
    String expectedMessage = "Username cannot be blank";
    Username username = new Username(null);

    Set<ConstraintViolation<Username>> validate = validator.validate(username);
    Set<String> result = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenUsernameContainsSpecialCharacters(){
    String expectedMessage = "Username cannot contain special characters: !@#$%^&*()";
    Username username = new Username("!@#$%^&*(),");

    Set<ConstraintViolation<Username>> validate = validator.validate(username);
    Set<String> result = validate.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }
}