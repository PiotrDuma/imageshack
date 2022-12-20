package com.github.PiotrDuma.imageshack.exception.badrequest;

import com.github.PiotrDuma.imageshack.exception.AbstractGlobalException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractGlobalException {

  public BadRequestException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
