package com.github.PiotrDuma.imageshack.AppUser.domain;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.exceptions.UserNotFoundException;
import com.github.PiotrDuma.imageshack.security.model.AppRoleRepo;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import com.github.PiotrDuma.imageshack.security.model.Role;
import java.util.Collection;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
class UserServiceImpl implements UserService {
  private final static String NOT_FOUND = "User %s not found";
  private final static String NOT_FOUND_BY_ID = "User with id= %d not found";
  private final AppRoleRepo appRoleRepo;
  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(AppRoleRepo appRoleRepo, UserRepository userRepo,
      PasswordEncoder passwordEncoder) {
    this.appRoleRepo = appRoleRepo;
    this.userRepo = userRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void addRole(Long userId, AppRoleType role) throws UserNotFoundException{
    User user = this.userRepo.getUserById(userId)
        .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND_BY_ID, userId)));
    Collection<Role> roles = user.getRoles();
    roles.add(appRoleRepo.findRoleByRoleType(role).get());
  }

  @Override
  public void removeRole(Long userId, AppRoleType role) {
    User user = this.userRepo.getUserById(userId)
        .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND_BY_ID, userId)));
    Collection<Role> roles = user.getRoles();
    roles.remove(appRoleRepo.findRoleByRoleType(role).get());
  }

  //TODO: separate username and email finding process + add validators.
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
  public UserDetailsWrapper createNewUser(String username, String email, String password) {
    UserDetailsWrapper wrapper = new UserDetailsWrapper.Builder(username,
        email,
        passwordEncoder.encode(password))
        .build();

//    already done in builder.
//    User user = userDetailsWrapper.getUser();
//    CustomUserDetails details = userDetailsWrapper.getCustomUserDetails();
//
//    user.setCustomUserDetails(details);
//    details.setUser(user);

    Long id = userRepo.save(wrapper.getUser()).getUserId().getId();
    this.addRole(id, AppRoleType.USER);
    return wrapper;
  }
}
