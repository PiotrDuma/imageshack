package com.github.PiotrDuma.imageshack.common;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailAddressTest {

  private Validator validator;

  @BeforeEach
  public void setUp(){
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  @Test
  void shouldFailValidationWhenEmailIsNull(){
    String expectedMessage = "Email cannot be blank";
    EmailAddress emailAddress = new EmailAddress(null);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailIsBlank(){
    String expectedMessage = "Email cannot be blank";
    EmailAddress emailAddress = new EmailAddress("");

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldPassWhenEmailIsValid(){
    String email = "ad2@d.com";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());
    System.out.println(result);
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldPassSomeSpecialSigns(){
    String email = "ad._12-34509792@d.com";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());
    System.out.println(result);
    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailValidationWhenEmailIsTooShort(){
    String expectedMessage = "Email must be longer that 3 signs";
    String email = "a@";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailIsTooLong(){
    String expectedMessage = "Email must be shorter that 64 signs";
    //65 char array
    String email = "H2TBaXDwTLLxZMCiTdd5ceqZdH0Npf2iRd9nmk5c7vQxYZqE3duKVSzKiYjFKTTag";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsManyAtSigns(){
    String expectedMessage = "Set valid email address";
    String email = "address@address@d.com";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsNoAtSign(){
    String expectedMessage = "Set valid email address";
    String email = "address.com";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldPassValidationWhenEmailHasDoubleDomainSuffix(){
    String expectedMessage = "Set valid email address";
    String email = "ad2@d.co.uk";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);

    assertTrue(violations.isEmpty());
  }

  @Test
  void shouldFailValidationWhenEmailContainsBlankCharacter(){
    String expectedMessage = "Set valid email address";
    String email = "a d2@d.com";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign0(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "$";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign1(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "\'";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign2(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "?";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign3(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "&";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign4(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "*";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign5(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "(";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign6(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = ")";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign7(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "\"";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign8(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "^";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign9(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "%";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign10(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "!";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign11(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = ",";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign12(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "<";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign13(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = ">";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign14(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "[";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }

  @Test
  void shouldFailValidationWhenEmailContainsProhibitedSign15(){
    String expectedMessage = "Email address cannot contain special signs: !%^$'?&*()\"";
    String email = "]";
    EmailAddress emailAddress = new EmailAddress(email);

    Set<ConstraintViolation<EmailAddress>> violations = validator.validate(emailAddress);
    Set<String> result = violations.stream()
        .map(ConstraintViolation::getMessage)
        .collect(Collectors.toSet());

    assertTrue(result.contains(expectedMessage));
  }
}