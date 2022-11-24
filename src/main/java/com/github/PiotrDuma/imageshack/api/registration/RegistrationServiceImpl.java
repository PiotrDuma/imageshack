package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationEmailSendingException.RegistrationEmailSendingException;
import com.github.PiotrDuma.imageshack.api.registration.Exceptions.RegistrationException.RegistrationException;
import com.github.PiotrDuma.imageshack.api.registration.RegistrationMessage.RegistrationMessage;
import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthFacade;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import com.github.PiotrDuma.imageshack.tools.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("registrationService")
class RegistrationServiceImpl implements RegistrationService {
  private final UserService userService;
  private final EmailService emailService;
  private final TokenAuthFacade tokenFacade;
  private final RegistrationMessage registrationMessage;

  @Autowired
  public RegistrationServiceImpl(UserService userService, EmailService emailService,
      TokenAuthFacade tokenFacade, RegistrationMessage registrationMessage) {
    this.userService = userService;
    this.emailService = emailService;
    this.tokenFacade = tokenFacade;
    this.registrationMessage = registrationMessage;
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
  }
}
