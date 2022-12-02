package com.github.PiotrDuma.imageshack.api.registration;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AppUserDTO {
  private static final String USERNAME_LENGTH = "Username must be between 3 and 20 characters";
  private static final String PASSWORD_LENGTH_MIN = "Password must have at least 8 characters";
  private static final String PASSWORD_LENGTH_MAX = "Password must have less than 32 characters";
  private static final String EMAIL_LENGTH = "Email address is too long";

  @NotBlank
  @Size(min = 3, max = 32, message = USERNAME_LENGTH)
  private String username;
  @Email
  @NotBlank
  @Size(max = 64, message = EMAIL_LENGTH)
  private String email;
  @NotBlank
  @Size(min = 8, message = PASSWORD_LENGTH_MIN)
  @Size(max = 32, message = PASSWORD_LENGTH_MAX)
  private String password;

  public AppUserDTO() {
  }

  public AppUserDTO(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
