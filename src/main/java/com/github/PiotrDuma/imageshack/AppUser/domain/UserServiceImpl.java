package com.github.PiotrDuma.imageshack.AppUser.domain;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.NoSuchRoleException;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Role;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.RoleService;
import com.github.PiotrDuma.imageshack.AppUser.domain.exceptions.UserNotFoundException;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class UserServiceImpl implements UserService {
  private final static String NOT_FOUND = "User %s not found";
  private final static String NOT_FOUND_BY_ID = "User with id= %d not found";
  private final UserRepository userRepo;
  private final RoleService roleService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public UserDetailsWrapper findUserById(java.lang.Long id) throws UsernameNotFoundException{
    User user = userRepo.findById(new Long(id)).orElseThrow(()-> new UserNotFoundException());
    return new UserDetailsWrapper(user);
  }

  //TODO: separate username and email finding process + add validators.
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, email)));
//    User user = userRepo.findByUsername(email)
//        .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, email)));
    return new UserDetailsWrapper(user);
  }

  @Override
  @Transactional
  public UserDetailsWrapper createNewUser(String username, String email, String password) {
    UserDetailsWrapper wrapper = new UserDetailsWrapper.Builder(username,
        email,
        passwordEncoder.encode(password))
        .build();

//  save relationship between user and details
    User user = wrapper.getUser();
    CustomUserDetails details = wrapper.getCustomUserDetails();

    user.setCustomUserDetails(details);
    details.setUser(user);

    Long id = userRepo.save(wrapper.getUser()).getId();
    this.addRole(id, AppRoleType.USER);
    return wrapper;
  }

  @Transactional
  public UserDetailsWrapper addRole(Long userId, AppRoleType roleType) throws UserNotFoundException {
    User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException());
    Set<Role> roles = user.getRoles();
    Role role = this.roleService.findRoleByRoleType(roleType);
    roles.add(role);
    return new UserDetailsWrapper(user);
  }

  @Transactional
  public UserDetailsWrapper removeRole(Long userId, AppRoleType roleType)
      throws UserNotFoundException, NoSuchRoleException {

    User user = userRepo.findById(userId)
      .orElseThrow(() -> new UserNotFoundException(String.format(NOT_FOUND_BY_ID, userId)));
    Set<Role> roles = user.getRoles();
    Role role = roleService.findRoleByRoleType(roleType);
    roles.remove(role);
    return new UserDetailsWrapper(user);
  }

  @Override
  public List<UserDetailsWrapper> findUsersByRole(AppRoleType roleType) {
    System.out.println(roleType.name());
    return userRepo.findAllByRole(roleType).stream()
        .map(k -> new UserDetailsWrapper(k))
        .collect(Collectors.toList());
  }
}
