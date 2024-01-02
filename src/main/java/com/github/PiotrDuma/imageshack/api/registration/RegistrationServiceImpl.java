package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterTransactionException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException;
import com.github.PiotrDuma.imageshack.tools.token.api.TokenObject;
import com.github.PiotrDuma.imageshack.tools.token.api.TokenAuthService;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("registrationService")
class RegistrationServiceImpl implements RegistrationService {
  @Override
  public void register(AppUserDTO appUserDTO) throws RegistrationException, RegisterIOException {
    //TODO:
  }

  @Override
  public void authenticate(String email, String tokenValue) throws RegistrationException {
    //TODO:
  }

  @Override
  public void sendAccountAuthenticationToken(String email) throws RegistrationException {
    //TODO:
  }
  /*  private final UserService userService;
  private final TokenAuthService tokenFacade;
  private final AuthTokenSender authTokenSender;
  private final Validator validator;
  private RegisterIOException exception;

  @Autowired
  public RegistrationServiceImpl(UserService userService, TokenAuthService tokenFacade,
      AuthTokenSender authTokenSender,
      @Qualifier("emailValidator") Validator validator) {
    this.userService = userService;
    this.tokenFacade = tokenFacade;
    this.authTokenSender = authTokenSender;
    this.validator = validator;
  }

  @Override
  @Transactional
  public void register(AppUserDTO dto) throws RegistrationException, RegisterIOException {
    this.exception = null; //clear exception cache with every request
    checkInputDTO(dto);
    if(exception!=null){
      throw exception;
    }
    try{
      this.userService.createNewUser(dto.getUsername(), dto.getEmail(), dto.getPassword());
    }catch(RuntimeException ex){
      throw new RegisterTransactionException(ex.getMessage(), ex);
    }
  }

  @Override
  @Transactional
  public void authenticate(String email, String tokenValue) throws RegistrationException{
    validateEmail(email);
    UserDetailsWrapper user = getUserDetailsWrapper(email);
    checkEnabled(user);

    TokenObject token = this.tokenFacade.findByEmail(email)
        .filter(t -> t.getTokenValue().equals(tokenValue))
        .filter(t -> t.getTokenType().equals(TokenAuthType.ACCOUNT_CONFIRMATION))
        .findAny().orElseThrow(() -> new RegistrationAuthException("Authentication token not found."));

    try{
      if(tokenFacade.isValid(token)){
        user.setEnabled(true);
      }else{
        throw new RuntimeException("Token expired.");
      }
    }catch (RuntimeException ex){
      throw new RegistrationAuthException("Authentication failed. " + ex.getMessage(), ex);
    }
  }

  @Override
  public void sendAccountAuthenticationToken(String email) throws RegistrationException {
    validateEmail(email);
    UserDetailsWrapper user = getUserDetailsWrapper(email);
    checkEnabled(user);

    authTokenSender.send(email, user.getUsername());
  }

  private void validateEmail(String email){
    if(!validator.validate(email)){
      throw new RegistrationAuthProcessingException("Invalid email address.");
    }
  }

  private void checkEnabled(UserDetails user){
    if(user.isEnabled()){
      throw new RegistrationAuthAccountException("Account's already enabled.");
    }
  }

  private UserDetailsWrapper getUserDetailsWrapper(String email){
    return userService.loadUserWrapperByUsername(email).orElseThrow(
        () -> new RegistrationAuthAccountException("User with email: "+ email +" not found."));
  }

  private void addExceptionMessage(AppUserDTO.Field field, String message){
    if(this.exception==null){
      this.exception = new RegisterIOException();
    }
    exception.addError(field, message);
  }

  *//**
   * AppUserDTO's fields are already checked by validators in controller.
   *//*
  private void checkInputDTO(AppUserDTO dto){
    if(userService.existsByUsername(dto.getUsername())){
      addExceptionMessage(AppUserDTO.Field.USERNAME, "User with login '"+ dto.getUsername()
          + "' already exists");
    }
    if(userService.existsByEmail(dto.getEmail())){
      addExceptionMessage(AppUserDTO.Field.EMAIL, "User with email '"+ dto.getEmail()
          + "' already exists");
    }
  }*/
}
