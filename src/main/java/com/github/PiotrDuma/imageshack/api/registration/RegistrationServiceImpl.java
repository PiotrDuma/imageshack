package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.tools.email.EmailSendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("registrationService")
class RegistrationServiceImpl implements RegistrationService {
  private final UserService userService;

  @Autowired
  public RegistrationServiceImpl(UserService userService) {
    this.userService = userService;
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
  public void register(AppUserDTO appUserDTO) throws RegistrationException {

  }

  @Override
  public boolean authenticate(String email, String tokenValue) throws EmailAuthenticationException {
    return false;
  }

  @Override
  public void sendConfirmationToken(String email) throws EmailSendingException {

  }
}
