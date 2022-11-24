package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.NothingToAuthException.NothingToAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException.RegistrationException;

public interface RegistrationService {
  void register(AppUserDTO appUserDTO) throws RegistrationException, RegistrationEmailSendingException;
  boolean authenticate(String email, String tokenValue) throws RegistrationAuthException;
  void sendAccountAuthenticationToken(String email) throws RegistrationEmailSendingException, NothingToAuthException;
}
