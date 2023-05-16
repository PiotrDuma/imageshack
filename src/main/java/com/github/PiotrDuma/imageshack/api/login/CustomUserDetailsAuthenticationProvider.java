package com.github.PiotrDuma.imageshack.api.login;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;

public class CustomUserDetailsAuthenticationProvider extends DaoAuthenticationProvider {
  private UserDetailsChecker postChecker;
  private UserDetailsChecker preChecker;
  public CustomUserDetailsAuthenticationProvider() {
    super();
    this.preChecker = new CustomPreAuthenticationChecks();
    this.postChecker = new CustomPostAuthenticationChecks();
    super.setPreAuthenticationChecks(preChecker);
    super.setPostAuthenticationChecks(postChecker);
  }

  private class CustomPreAuthenticationChecks implements UserDetailsChecker {

    /**
     * Avoid UserDetails' flag checks before credentials' authentication by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider#additionalAuthenticationChecks(
     * UserDetails, UsernamePasswordAuthenticationToken)} in
     * {@link org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider#authenticate(
     * Authentication)} method.
     */
    @Override
    public void check(UserDetails user) {
      //DO NOTHING
    }
  }

  private class CustomPostAuthenticationChecks implements UserDetailsChecker {

    /**
     * Validate UserDetails' flags removed from {@link CustomPreAuthenticationChecks#check(UserDetails)}
     * and optionally throw appropriate exceptions.
     */
    @Override
    public void check(UserDetails user) {
      if (!user.isAccountNonLocked()) {
        logger.debug("Failed to authenticate since user account is locked");
        throw new LockedException("User account is locked");
      }
      if (!user.isEnabled()) {
        logger.debug("Failed to authenticate since user account is disabled");
        throw new DisabledException("User is disabled");
      }
    }
  }
}
