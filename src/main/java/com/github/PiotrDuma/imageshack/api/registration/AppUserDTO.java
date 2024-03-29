package com.github.PiotrDuma.imageshack.api.registration;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class AppUserDTO {
  @NotBlank(message = "Username cannot be blank")
  @Size(min = 3, max = 32, message = "Username must be between 3-32 characters")
  private String username;
  @Email(message = "It's not email format")
  @NotBlank(message = "Email cannot be blank")
  @Size(max = 64, message = "Email address is too long")
  private String email;
  @NotBlank(message = "Password cannot be blank")
  @Size(min = 8, message = "Password must have at least 8 characters")
  @Size(max = 32, message = "Password must have less than 32 characters")
  private String password;
  public AppUserDTO() {
  }

  public AppUserDTO(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public enum Field{
    USERNAME("username"),
    EMAIL("email"),
    PASSWORD("password");

    private final String fieldName;

    Field(String fieldName) {
      this.fieldName = fieldName;
    }

    public String getFieldName() {
      return fieldName;
    }
  }

  /**
   * Return AppUserDTO field value by given Field argument.
   * Preferable usage in controller to receive posted values when an IO exception is thrown.
   *
   * @param field corresponding enum Field type of desired AppUserDTO field's value.
   *
   * @return returns value of given field. If field is not recognized or is a password, returns
   * empty string.
   */
  public String getValue(AppUserDTO.Field field){
    if(field == Field.EMAIL) return email;
    if(field == Field.USERNAME) return username;
    return ""; //return password as empty string.
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
