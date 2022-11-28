package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationAccountEnabledException.RegistrationAccountEnabledException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailAddressException.RegistrationEmailAddressException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuthType;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenAuthDTO;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject.TokenObject;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.InvalidEmailAddressException;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("registrationService")
class RegistrationServiceImpl implements RegistrationService {
  @Value("${spring.application.name}") private String appName;
  private final UserService userService;
  private final EmailService emailService;
  private final TokenAuthFacade tokenFacade;
  private final RegistrationMessage registrationMessage;
  private final Validator validator;

  @Autowired
  public RegistrationServiceImpl(UserService userService, EmailService emailService,
      TokenAuthFacade tokenFacade, RegistrationMessage registrationMessage,
      @Qualifier("emailValidator") Validator validator) {
    this.userService = userService;
    this.emailService = emailService;
    this.tokenFacade = tokenFacade;
    this.registrationMessage = registrationMessage;
    this.validator = validator;
  }

  //TODO: extend registration by email confirmation
//  @Override
//  public void registerUser(AppUserDTO appUserDTO) {
//    UserDetailsWrapper wrapper = userManageService.createNewUser(
//        appUserDTO.getEmail(),
//        appUserDTO.getUsername(),
//        appUserDTO.getPassword());
//  }


  @Override
  public void register(AppUserDTO appUserDTO){

  }

  @Override
  public boolean authenticate(String email, String tokenValue){
    return false;
  }

  @Override
  public void sendAccountAuthenticationToken(String email){
    validateEmail(email);
    UserDetailsWrapper user = getUserDetailsWrapper(email);
    checkEnabled(user);

    String subject = appName + ": account activation";
    TokenObject token = this.tokenFacade.create(new TokenAuthDTO(email, TokenAuthType.ACCOUNT_CONFIRMATION));
    Instant expirationDate = this.tokenFacade.expiresAt(token); //TODO:
    String message = registrationMessage.generate(email, user.getUsername(), token.getTokenValue(),
        expirationDate,  false);
    sendEmail(email, subject, message);
  }

  private void validateEmail(String email){
    if(!validator.validate(email)){
      throw new RegistrationEmailAddressException();
    }
  }
  private void checkEnabled(UserDetails user){
    if(user.isEnabled()){
      throw new RegistrationAccountEnabledException();
    }
  }
  private UserDetailsWrapper getUserDetailsWrapper(String email){
    return userService.loadUserWrapperByUsername(email).orElseThrow(//TODO:
        () -> new RegistrationEmailAddressException());
  }
  private void sendEmail(String email, String subject, String message){
    try{
      this.emailService.sendMail(email, subject, message, false);
    }catch (EmailSendingException ex){
      throw new RegistrationEmailSendingException(ex.getMessage());
    }catch(InvalidEmailAddressException ex){
      throw new RegistrationEmailAddressException();
    }
  }
}
