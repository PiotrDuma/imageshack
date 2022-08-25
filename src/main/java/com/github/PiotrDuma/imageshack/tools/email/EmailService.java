package com.github.PiotrDuma.imageshack.tools.email;

public interface EmailService {
  boolean sendMail(String to, String subject, String message, boolean isHTML);
}
