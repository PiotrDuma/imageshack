package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.NoSuchRoleException;
import com.github.PiotrDuma.imageshack.AppUser.domain.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.AppUser.domain.exceptions.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
  UserDetailsWrapper findUserById(Long id);
  UserDetailsWrapper createNewUser(String username, String email, String password);
  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
  UserDetailsWrapper addRole(Long userId, AppRoleType roleType) throws UserNotFoundException, NoSuchRoleException;
  UserDetailsWrapper removeRole(Long userId, AppRoleType roleType) throws UserNotFoundException;
  List<UserDetailsWrapper> findUsersByRole(AppRoleType roleType);
  Optional<UserDetailsWrapper> loadUserWrapperByUsername(String username);
}
