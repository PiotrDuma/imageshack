package com.github.PiotrDuma.imageshack.common;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class EmailAddress {
  private static final int MIN_LENGTH = 3;
  private static final int  MAX_LENGTH = 64;
  private static final String PROHIBITED_SIGNS_PATTERN ="^(?!.*(<|>|[\\?]))[a-zA-Z0-9_.\\-@]+$";
  private static final String EMAIL_EXCEPTION = "Set valid email address";
  private static final String PATTERN_EXCEPTION =  "Email address cannot contain special signs: !%^$\'?&*()\"";
  private static final String MIN_LENGTH_EXCEPTION = "Email must be longer that 3 signs";
  private static final String MAX_LENGTH_EXCEPTION = "Email must be shorter that 64 signs";
  private static final String NULL_EXCEPTION = "Email cannot be blank";

  @NotBlank(message = NULL_EXCEPTION)
  @Size(min = MIN_LENGTH, message = MIN_LENGTH_EXCEPTION)
  @Size(max = MAX_LENGTH, message = MAX_LENGTH_EXCEPTION)
  @Pattern(regexp = PROHIBITED_SIGNS_PATTERN, message = PATTERN_EXCEPTION)
  @Email(message = EMAIL_EXCEPTION)
  private String email;

  public EmailAddress(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
