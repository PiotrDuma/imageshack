package com.github.PiotrDuma.imageshack.tools.validators.validators.PasswordValidator;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.validators.PasswordValidator.PasswordValidator;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = new PasswordValidator();
  }

  @Test
  void returnFalseWhenPasswordHasNoDigit(){
    String password = "Password";
    assertFalse(this.validator.validate(password));
  }

  @Test
  void returnFalseWhenPasswordHasNoUpperCase(){
    String password = "password123";
    assertFalse(this.validator.validate(password));
  }

  @Test
  void returnFalseWhenPasswordHasNoLowerCase(){
    String password = "PASSWORD123";
    assertFalse(this.validator.validate(password));
  }

  @Test
  void returnFalseWhenPasswordHasBlankSigns(){
    String password1 = "Pass word123";
    String password2 = "Password123 ";
    String password3 = " Password123";
    assertFalse(this.validator.validate(password1));
    assertFalse(this.validator.validate(password2));
    assertFalse(this.validator.validate(password3));
  }

  @Test
  void returnTrueWhenPasswordIsCorrectAndHasSpecialSigns(){
    String password = "PASsswORD123,)";
    assertTrue(this.validator.validate(password));
  }

  @Test
  void returnTrueWhenPasswordIsPassMinimalStatements(){
    String password = "Password1";
    assertTrue(this.validator.validate(password));
  }

  @Test
  void returnFalseWhenPasswordIsTooShort(){
    String password = "PAss1";
    assertFalse(this.validator.validate(password));
  }

  @Test
  void returnFalseWhenPasswordIsEmpty(){
    String password = "";
    assertFalse(this.validator.validate(password));
  }

  @Test
  void returnFalseWhenPasswordIsTooLong(){
    String password = new RandomString(33).nextString();
    assertFalse(this.validator.validate(password));
  }

  @Test
  void getExceptionMessageReturnsValidString(){
    String message = "Password size must be between 8-32, it has to "
        + "have at least one lower-, one upper case letter, one digit and "
        + "cannot contain blank signs";
    assertEquals(message, this.validator.getExceptionMessage());
  }
}