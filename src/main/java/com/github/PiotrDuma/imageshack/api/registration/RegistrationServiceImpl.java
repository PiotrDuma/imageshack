package com.github.PiotrDuma.imageshack.api.registration;

import com.github.PiotrDuma.imageshack.AppUser.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.AppUser.UserRepository;
import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.api.registration.api.AppUserDTO;
import com.github.PiotrDuma.imageshack.api.registration.api.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("registrationService") //TODO: encapsulate service to private class
public class RegistrationServiceImpl implements RegistrationService {
  private final UserService userService;

  @Autowired
  public RegistrationServiceImpl(UserService userService) {
    this.userService = userService;
  }

  //TODO: extend registration by email confirmation
  @Override
  public void registerUser(AppUserDTO appUserDTO) {
    UserDetailsWrapper wrapper = userService.createNewUser(
        appUserDTO.getEmail(),
        appUserDTO.getUsername(),
        appUserDTO.getPassword());
  }
}
