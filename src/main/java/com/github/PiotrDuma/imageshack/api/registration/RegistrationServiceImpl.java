package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegisterIOException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthAccountException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAuthProcessingException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("registrationService")
class RegistrationServiceImpl implements RegistrationService {
  private final UserService userService;
  private final TokenAuthFacade tokenFacade;
  private final AuthTokenSender authTokenSender;
  private final Validator validator;

  @Autowired
  public RegistrationServiceImpl(UserService userService, TokenAuthFacade tokenFacade,
      AuthTokenSender authTokenSender,
      @Qualifier("emailValidator") Validator validator) {
    this.userService = userService;
    this.tokenFacade = tokenFacade;
    this.authTokenSender = authTokenSender;
    this.validator = validator;
  }

  @Override
  @Transactional
  public void register(AppUserDTO dto) throws RegisterIOException {
    boolean loginExists = userService.existsByUsername(dto.getUsername());
    boolean emailExists = userService.existsByEmail(dto.getEmail());
    if(loginExists || emailExists) {
      throw new RegisterIOException(loginExists, emailExists);
    }
    UserDetailsWrapper newUser = this.userService.createNewUser(dto.getUsername(), dto.getEmail(),
        dto.getPassword());
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
      throw new RegistrationAuthException("Authentication failed. " + ex.getMessage(), ex.getCause());
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
}
