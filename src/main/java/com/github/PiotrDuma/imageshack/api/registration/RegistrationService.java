package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;

public interface RegistrationService {
  void register(AppUserDTO appUserDTO) throws RuntimeException, RegisterIOException;
  void authenticate(String email, String tokenValue) throws RegistrationAuthException;
  void sendAccountAuthenticationToken(String email) throws RegistrationEmailSendingException;
}
