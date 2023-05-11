package com.github.PiotrDuma.imageshack.api.login;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class CustomUserDetailsAuthenticationProvider extends DaoAuthenticationProvider {
  private UserDetailsChecker checker;
  public CustomUserDetailsAuthenticationProvider() {
    super();
    this.checker = new CustomPreAuthenticationChecks();
    super.setPreAuthenticationChecks(checker);
  }

  private class CustomPreAuthenticationChecks implements UserDetailsChecker{

    /**
     * Customize exceptions thrown with chosen UserDetail's values.
     */
    @Override
    public void check(UserDetails user) {
      if (!user.isAccountNonLocked()) {
        logger.debug("Failed to authenticate since user account is locked");
        throw new LockedException("User account is locked");
      }
      if(!user.isEnabled()){
        logger.debug("Failed to authenticate since user account is disabled");
        throw new DisabledException("User is disabled");
      }
    }
  }
}
