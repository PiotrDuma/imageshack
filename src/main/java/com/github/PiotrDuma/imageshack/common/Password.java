package com.github.PiotrDuma.imageshack.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Password {
  private static final int MIN_LENGTH = 8;
  private static final int  MAX_LENGTH = 32;
  private static final String MIN_LENGTH_EXCEPTION = "Password must contain at least 8 characters";
  private static final String MAX_LENGTH_EXCEPTION = "Password must contain max 32 characters";
  private static final String NULL_EXCEPTION = "Password cannot be blank";
  private static final String LOWERCASE_EXCEPTION = "Password must contain at least one lowercase character";
  private static final String UPPERCASE_EXCEPTION = "Password must contain at least one uppercase character";
  private static final String DIGIT_EXCEPTION = "Password must contain at least one digit";
  private static final String BLANK_SIGN_EXCEPTION = "Password cannot contain blank characters";
  private static final String REQUIRE_DIGIT =".*\\d.*";
  private static final String REQUIRE_LOWER_CASE = ".*[a-z].*";
  private static final String REQUIRE_UPPER_CASE = ".*[A-Z].*";
  private static final String REQUIRE_NO_BLANK_SIGNS = "[\\S]+$";

  @NotBlank(message = NULL_EXCEPTION)
  @Size(min = MIN_LENGTH, message = MIN_LENGTH_EXCEPTION)
  @Size(max = MAX_LENGTH, message = MAX_LENGTH_EXCEPTION)
  @Pattern(regexp = REQUIRE_DIGIT, message = DIGIT_EXCEPTION)
  @Pattern(regexp = REQUIRE_LOWER_CASE, message = LOWERCASE_EXCEPTION)
  @Pattern(regexp = REQUIRE_UPPER_CASE, message = UPPERCASE_EXCEPTION)
  @Pattern(regexp = REQUIRE_NO_BLANK_SIGNS, message = BLANK_SIGN_EXCEPTION)
  private String value;

  public Password(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}