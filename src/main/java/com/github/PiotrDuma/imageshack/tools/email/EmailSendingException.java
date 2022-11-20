package com.github.PiotrDuma.imageshack.tools.email;

public class EmailSendingException extends RuntimeException{
  public EmailSendingException(String message) {
    super(message);
  }
  public EmailSendingException(String message, Throwable cause){
    super(message, cause);
  }
}
