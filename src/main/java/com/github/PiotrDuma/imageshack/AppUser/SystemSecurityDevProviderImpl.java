package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.SecurityRoleConfiguration;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("devSecurityConfiguration")
class SystemSecurityDevProviderImpl implements SystemSecurityProvider {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final SecurityRoleConfiguration roleConfig;

  @Autowired
  public SystemSecurityDevProviderImpl(UserService userService, PasswordEncoder passwordEncoder,
      SecurityRoleConfiguration roleConfig) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.roleConfig = roleConfig;
  }

  @Override
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void generateSystemUsers() {
    roleConfig.setupSecurityRoleConfiguration();

    if (userService.findUsersByRole(AppRoleType.OWNER).isEmpty()) {
      System.out.println("START CREATING SYSTEM OWNER");
      UserDetailsWrapper owner = userService.createNewUser("owner",
          "owner@imageshack.com", "passwd");
      userService.addRole(owner.getUserId(), AppRoleType.OWNER);
    }
  }
}
