package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.SecurityRoleConfiguration;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("devSecurityConfiguration")
@Profile({"dev", "test"})
class SystemSecurityUserProviderConfigurationDev {
  private final UserService userService;
  private final SecurityRoleConfiguration roleConfig;

  @Autowired
  public SystemSecurityUserProviderConfigurationDev(UserService userService,
                      SecurityRoleConfiguration roleConfig) {
    this.userService = userService;
    this.roleConfig = roleConfig;
  }

  //TODO: prepare demo user accounts with different roles.
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  private void generateSystemUsers() {
    roleConfig.setupSecurityRoleConfiguration();

    if (userService.findUsersByRole(AppRoleType.OWNER).isEmpty()) {
      System.out.println("START CREATING SYSTEM OWNER");
      UserDetailsWrapper owner = userService.createNewUser("owner",
          "owner@imageshack.com", "passwd123");
      userService.addRole(owner.getUserId(), AppRoleType.OWNER);
    }
  }
}
