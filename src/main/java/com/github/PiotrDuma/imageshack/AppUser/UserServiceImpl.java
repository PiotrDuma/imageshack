package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.AppUser.UserDetails.CustomUserDetails;
import com.github.PiotrDuma.imageshack.AppUser.UserDetails.UserDetailsRepository;
import com.github.PiotrDuma.imageshack.security.model.AppRoleRepo;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import com.github.PiotrDuma.imageshack.security.model.Role;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {
  private final static String NOT_FOUND = "USER %s NOT FOUND";
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
  }

  @Override
  public void removeRole(User user, AppRoleType role) {
    Collection<Role> roles = user.getRoles();
    roles.remove(appRoleRepo.findRoleByRoleType(role).get());
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, email)));
//    User user = userRepo.findByUsername(email)
//        .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, email)));
    System.out.println(user.getCustomUserDetails().toString());
    return new UserDetailsWrapper(user);
  }

  @Override
  public UserDetailsWrapper createNewUser(String email, String username, String password) {
    UserDetailsWrapper userDetailsWrapper = new UserDetailsWrapper.Builder(username,email,password)
        .build();
    addRole(userDetailsWrapper.getUser(), AppRoleType.USER);

    User user = userDetailsWrapper.getUser();
    CustomUserDetails details = userDetailsWrapper.getCustomUserDetails();

    user.setCustomUserDetails(details);
    details.setUser(user);

    userRepo.save(userDetailsWrapper.getUser());
    return userDetailsWrapper;
  }
}
