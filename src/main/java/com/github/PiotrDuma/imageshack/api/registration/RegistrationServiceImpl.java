package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
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
}
