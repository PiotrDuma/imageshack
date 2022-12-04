package com.github.PiotrDuma.imageshack.tools.validators.validators.EmailValidator;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.EmailValidator;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailValidatorTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
  this.validator = new EmailValidator();
  }

  @Test
  void returnFalseWhenEmailIsTooShort(){
    String email = "@e";

    assertFalse(validator.validate(email));
  }

  @Test
  void returnFalseWhenEmailIsTooLong(){
    String email = new RandomString(255).nextString() +"@imageshack.com";
    assertTrue(email.length() >254);
    assertFalse(validator.validate(email));
  }

  @Test
  void returnFalseWhemEmailNotValidPattern(){
    String email = "some@Email@google.com";

    assertFalse(validator.validate(email));
  }

  @Test
  void returnFalseWhenEmailHasNoDomain(){
    String email = "login@";
    assertFalse(validator.validate(email));
  }

  @Test
  void returnFalseWhenEmailHasNoLogin(){
    String email = "@imageshack.org";
    assertFalse(validator.validate(email));
  }

  @Test
  void returnFalseWhenEmailHasNoDomainExtension(){
    String email = "login@imageshack.";
    assertFalse(validator.validate(email));
  }

  @Test
  void returnTrueIfEmailPassStatements(){
    String email = "login123@imageshack.net";
    assertTrue(validator.validate(email));
  }

  @Test
  void returnFalseWhenEmailHasForbiddenSpecialSigns(){
    String email = "aaaa@email.com";
    assertFalse(this.validator.validate("$"+email));
    assertFalse(this.validator.validate("!"+email));
    assertFalse(this.validator.validate("&"+email));
    assertFalse(this.validator.validate("*"+email));
    assertFalse(this.validator.validate("("+email));
    assertFalse(this.validator.validate(")"+email));
    assertFalse(this.validator.validate("\""+email));
    assertFalse(this.validator.validate("\'"+email));
  }

  @Test
  void returnFalseWhenIfEmailContainsBlankSigns(){
    assertFalse(validator.validate("login123@imageshack.net "));
    assertFalse(validator.validate(" login123@imageshack.net"));
    assertFalse(validator.validate("login123@ imageshack.net"));
    assertFalse(validator.validate("login 123@imageshack.net"));
  }

  @Test
  void getExceptionMessageReturnsValidString(){
    String message = "Email address must be between 3-64 characters, "
        + "it cannot contain blank and special signs: $\'?@&*()\"";
    assertEquals(message, this.validator.getExceptionMessage());
  }
}