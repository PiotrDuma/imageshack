package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAccountEnabledException.RegistrationAccountEnabledException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailAddressException.RegistrationEmailAddressException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException.RegistrationException;

public interface RegistrationService {
  void register(AppUserDTO appUserDTO) throws RegistrationException, RegistrationEmailSendingException;
  boolean authenticate(String email, String tokenValue) throws RegistrationAuthException,
      RegistrationAccountEnabledException;
  void sendAccountAuthenticationToken(String email) throws RegistrationEmailSendingException,
      RegistrationEmailAddressException, RegistrationAccountEnabledException;
}
