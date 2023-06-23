package com.github.PiotrDuma.imageshack.api.passwordreset;

public interface PasswordResetService {

  void authenticate(String email, String token) throws RuntimeException; //TODO: exception
}
