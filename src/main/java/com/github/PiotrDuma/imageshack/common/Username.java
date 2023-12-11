package com.github.PiotrDuma.imageshack.common;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Username {
  private static final int MIN_LENGTH = 3;
  private static final int  MAX_LENGTH = 32;
  private static final String PATTERN = "[a-zA-Z\\d_.\\-]+$";
  private static final String MIN_LENGTH_EXCEPTION = "Username must have at least 3 characters";
  private static final String MAX_LENGTH_EXCEPTION = "Username must contain less than 32 characters";
  private static final String PATTERN_EXCEPTION = "Username cannot contain special characters: !@#$%^&*()";
  private static final String NULL_EXCEPTION = "Username cannot be blank";

  @NotBlank(message = NULL_EXCEPTION)
  @Size(min = MIN_LENGTH, message = MIN_LENGTH_EXCEPTION)
  @Size(max = MAX_LENGTH, message = MAX_LENGTH_EXCEPTION)
  @Pattern(regexp = PATTERN, message = PATTERN_EXCEPTION)
  private String username;

  public Username(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
