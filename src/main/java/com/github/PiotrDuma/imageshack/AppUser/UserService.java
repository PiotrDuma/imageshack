package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.security.model.AppRoleType;

public interface UserService {

  void addRole(User user, AppRoleType role);
  void removeRole(User user, AppRoleType role);
  UserDetailsWrapper createNewUser(String username, String email, String password);
}
