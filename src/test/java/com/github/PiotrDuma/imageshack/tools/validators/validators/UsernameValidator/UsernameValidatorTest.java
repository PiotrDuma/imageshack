package com.github.PiotrDuma.imageshack.tools.validators.validators.UsernameValidator;

import static org.junit.jupiter.api.Assertions.*;

import com.github.PiotrDuma.imageshack.tools.validators.UsernameValidator.UsernameValidator;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsernameValidatorTest {
  private Validator validator;

  @BeforeEach
  void setUp(){
    this.validator = new UsernameValidator();
  }

  @Test
  void returnFalseWhenUsernameIsTooShort(){
    String username = "aa";
    assertFalse(this.validator.validate(username));
  }

  @Test
  void returnFalseWhenUsernameIsTooLong(){
    String username = new RandomString(65).nextString();
    assertFalse(this.validator.validate(username));
  }

  @Test
  void returnFalseWhenUsernameHasBlankSigns(){
    String username1 = "aa  aaa";
    String username2 = "aaaaa ";
    String username3 = " aaaaa";
    assertFalse(this.validator.validate(username1));
    assertFalse(this.validator.validate(username2));
    assertFalse(this.validator.validate(username3));
  }

  @Test
  void returnFalseWhenUsernameHasForbiddenSpecialSigns(){
    String username = "aaaa";
    assertFalse(this.validator.validate(username+"%"));
    assertFalse(this.validator.validate(username+"$"));
    assertFalse(this.validator.validate(username+"!"));
    assertFalse(this.validator.validate(username+"@"));
    assertFalse(this.validator.validate(username+"&"));
    assertFalse(this.validator.validate(username+"*"));
    assertFalse(this.validator.validate(username+"("));
    assertFalse(this.validator.validate(username+")"));
    assertFalse(this.validator.validate(username+"\""));
    assertFalse(this.validator.validate(username+"\'"));
  }

  @Test
  void returnTrueWhenUsernameHasAllowedSpecialSigns(){
    String username = "aaaa";
    assertTrue(this.validator.validate(username+"_"));
    assertTrue(this.validator.validate(username+"-"));
    assertTrue(this.validator.validate(username+"."));
  }

  @Test
  void returnTrueWhenUsernameIsValid(){
    String username = "AAAAaaaa123";
    assertTrue(this.validator.validate(username));
  }

  @Test
  void returnFalseWhenUsernameIsEmpty(){
    String username = "";
    assertFalse(this.validator.validate(username));
  }

  @Test
  void getExceptionMessageReturnsValidString(){
    String message = "Username must be between 3-32 characters, "
        + "cannot contain blank and special signs: %\'$@&*()\"";
    assertEquals(message, this.validator.getExceptionMessage());
  }
}