package com.github.PiotrDuma.imageshack.exception.type;

import com.github.PiotrDuma.imageshack.exception.AbstractGlobalException;
import org.springframework.http.HttpStatus;

/**
 * returns 404
 */
public class NotFoundException extends AbstractGlobalException{

  public NotFoundException(String message){
    super(message, HttpStatus.NOT_FOUND);
  }
}
