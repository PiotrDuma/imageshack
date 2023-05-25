package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException;

public interface RegistrationService {
  void register(AppUserDTO appUserDTO) throws RegistrationException, RegisterIOException;
  void authenticate(String email, String tokenValue) throws RegistrationException;

  /**
   * Method sends authorization link by email message required to activate system account and
   * confirm identity.
   *
   * Method generates activation token of registered account and sends system message to the
   * provided email address.
   *
   * @param email - email address of account, which has to be activated
   *
   * @throws RegistrationException - specified exception depending on method processing failure.
   *
   * @see com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException
   * @see com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException
   */
  void sendAccountAuthenticationToken(String email) throws RegistrationException;
}
