package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import java.io.IOException;

public interface RegistrationService {
  void register(AppUserDTO appUserDTO) throws RuntimeException;
  void authenticate(String email, String tokenValue) throws RegistrationAuthException;
  void sendAccountAuthenticationToken(String email) throws RegistrationEmailSendingException;
}
