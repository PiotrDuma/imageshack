package com.github.PiotrDuma.imageshack.api.registration.Exceptions;

import com.github.PiotrDuma.imageshack.tools.token.api.TokenAuthService;

/**
 * Exception processing due to errors in external services. E.g. failed email sending or token
 * initialization.
 *
 * @see com.github.PiotrDuma.imageshack.tools.email.EmailService EmailService
 * @see TokenAuthService TokenAuthFacade
 */
public class RegistrationAuthProcessingException extends RegistrationException{

  public RegistrationAuthProcessingException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public RegistrationAuthProcessingException(String msg) {
    super(msg);
  }
}
