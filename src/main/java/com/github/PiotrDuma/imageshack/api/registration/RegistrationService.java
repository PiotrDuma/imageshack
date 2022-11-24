package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;

public interface RegistrationService {
  void register(AppUserDTO appUserDTO) throws RegistrationException, EmailSendingException;
  boolean authenticate(String email, String tokenValue) throws EmailAuthenticationException;
  void sendConfirmationToken(String email) throws EmailSendingException;
}
