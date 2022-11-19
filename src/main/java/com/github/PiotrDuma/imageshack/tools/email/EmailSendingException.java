package com.github.PiotrDuma.imageshack.tools.email;

public class EmailSendingException extends RuntimeException{

  public EmailSendingException() {
  }

  public EmailSendingException(String message) {
    super(message);
  }
}
