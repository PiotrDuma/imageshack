package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

  void addRole(User user, AppRoleType role);
  void removeRole(User user, AppRoleType role);
  UserDetailsWrapper createNewUser(String username, String email, String password);
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
