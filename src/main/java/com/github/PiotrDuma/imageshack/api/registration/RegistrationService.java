package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException;

public interface RegistrationService {

  /**
   * Validates provided data and creates new system user.
   *
   * @param appUserDTO an object with data to register system user
   *
   * @throws com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterTransactionException
   * if process of creating system user fails
   * @throws RegisterIOException if provided AppUserDTO is rejected (i.e. username/email's already
   * taken). Exception thrown contains information about exception attached to corresponding
   * AppUserDTO's field.
   *
   * @see RegisterIOException
   */
  void register(AppUserDTO appUserDTO) throws RegistrationException, RegisterIOException;

  /**
   * Activates system account with given unique email address by generated token. Required arguments
   * are sent with email using {@link #sendAccountAuthenticationToken} method. Parameters are
   * encoded in URL link in the message.
   *
   * The method checks provided parameters and if account is not enabled, the token is being searched
   * by email, token value and token type. Account activates if token meets the requirements.
   *
   *
   * @param email the email address of account to activate
   * @param tokenValue the provided token value
   *
   * @throws com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException
   * if provided token is invalid or processing changes fails
   * @throws com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException
   * if provided email is invalid
   * @throws com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException
   * if model with given email is not found, not exist, or account is already enabled
   */
  void authenticate(String email, String tokenValue) throws RegistrationException;

  /**
   * Method sends authorization URL link by email message required to activate system account and
   * confirm identity.
   *
   * Method generates activation token of registered account and sends system message to the
   * provided email address.
   *
   * @param email email address of account, which has to be activated.
   *
   * @throws com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException
   * in case the email sending or token generation failure
   * @throws com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException
   * if model with given email is not found, not exist, or account is already enabled
   */
  void sendAccountAuthenticationToken(String email) throws RegistrationException;
}
