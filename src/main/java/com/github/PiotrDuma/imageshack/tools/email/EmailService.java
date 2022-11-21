package com.github.PiotrDuma.imageshack.tools.email;

import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;

public interface EmailService {
  void sendMail(String to, String subject, String message, boolean isHTML)
      throws InvalidEmailAddressException, EmailSendingException;
}
