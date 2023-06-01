package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.AppUser.domain.exceptions.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//TODO: Check exception handling.
public interface UserService extends UserDetailsService {
  UserDetailsWrapper findUserById(Long id);
  UserDetailsWrapper createNewUser(String username, String email, String password);
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
  UserDetailsWrapper addRole(Long userId, AppRoleType roleType) throws RuntimeException;
  UserDetailsWrapper removeRole(Long userId, AppRoleType roleType) throws UserNotFoundException;
  List<UserDetailsWrapper> findUsersByRole(AppRoleType roleType);
  Optional<UserDetailsWrapper> loadUserWrapperByUsername(String username);
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
}
