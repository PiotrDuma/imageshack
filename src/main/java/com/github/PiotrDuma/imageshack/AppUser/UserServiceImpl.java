package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.security.model.AppRoleRepo;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import com.github.PiotrDuma.imageshack.security.model.Role;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private final AppRoleRepo appRoleRepo;
  private final UserRepository userRepo;

  @Autowired
  public UserServiceImpl(AppRoleRepo appRoleRepo, UserRepository userRepo) {
    this.appRoleRepo = appRoleRepo;
    this.userRepo = userRepo;
  }

  @Override
  public void addRole(User user, AppRoleType role) {
    Collection<Role> roles = user.getRoles();
    roles.add(appRoleRepo.findRoleByRoleType(role).get());
    userRepo.save(user);
  }

  @Override
  public void removeRole(User user, AppRoleType role) {
    Collection<Role> roles = user.getRoles();
    roles.remove(appRoleRepo.findRoleByRoleType(role).get());
    userRepo.save(user);
  }

  @Override
  public UserDetailsWrapper createNewUser(String username, String email, String password) {
    UserDetailsWrapper userDetailsWrapper = new UserDetailsWrapper.Builder(username,email,password)
        .build();
    addRole(userDetailsWrapper.getUser(), AppRoleType.USER);
    return userDetailsWrapper;
  }
}
