package com.github.PiotrDuma.imageshack.api.registration.Exceptions;


//TODO: map of exception failures
public class RegisterIOException extends RegistrationException {
  private static final String ALREADY_EXISTS = "That username/email has been taken";
  private static final String EMAIL_TAKEN = "Email has been taken";
  private static final String USERNAME_TAKEN = "Username has been taken";
  private final boolean isLoginTaken;
  private final boolean isEmailTaken;
  public RegisterIOException(boolean isLoginTaken, boolean isEmailTaken) {
    super(ALREADY_EXISTS);
    this.isLoginTaken = isLoginTaken;
    this.isEmailTaken = isEmailTaken;
  }

  public boolean isLoginTaken() {
    return isLoginTaken;
  }

  public boolean isEmailTaken() {
    return isEmailTaken;
  }

  public String getUsernameTakenMessage(){
    return USERNAME_TAKEN;
  }

  public String getEmailTakenMessage(){
    return EMAIL_TAKEN;
  }
}
