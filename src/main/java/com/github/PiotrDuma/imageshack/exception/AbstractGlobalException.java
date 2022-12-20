package com.github.PiotrDuma.imageshack.exception;

import java.time.ZonedDateTime;
import org.springframework.http.HttpStatus;

public class AbstractGlobalException extends RuntimeException{
  private final String message;
  private final HttpStatus status;
  private final ZonedDateTime timestamp;

  public AbstractGlobalException(String message, HttpStatus status) {
    super(message);
    this.message = message;
    this.status = status;
    this.timestamp = ZonedDateTime.now();
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public ZonedDateTime getTimestamp() {
    return timestamp;
  }

  public ExceptionDTO getExceptionInfo(){
    return new ExceptionDTO(message, status, timestamp);
  }
}
