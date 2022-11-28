package com.github.PiotrDuma.imageshack.AppUser.domain;

import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.NoSuchRoleException;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Role;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.RoleService;
import com.github.PiotrDuma.imageshack.AppUser.domain.exceptions.UserNotFoundException;
import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import com.github.PiotrDuma.imageshack.tools.validators.EmailValidator.EmailValidator;
import com.github.PiotrDuma.imageshack.tools.validators.UsernameValidator.UsernameValidator;
import com.github.PiotrDuma.imageshack.tools.validators.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
  private final Validator emailValidator;
  private final Validator usernameValidator;

  @Autowired
  public UserServiceImpl(UserRepository userRepo, RoleService roleService, PasswordEncoder passwordEncoder,
                        @Qualifier("emailValidator") Validator emailValidator,
                        @Qualifier("usernameValidator") Validator usernameValidator) {
    this.userRepo = userRepo;
    this.roleService = roleService;
    this.passwordEncoder = passwordEncoder;
    this.emailValidator = emailValidator;
    this.usernameValidator = usernameValidator;
  }

  @Override
  public UserDetailsWrapper findUserById(Long id) throws UsernameNotFoundException{
    User user = userRepo.findById(id).orElseThrow(()-> new UserNotFoundException(String.format(NOT_FOUND_BY_ID, id)));
    return new UserDetailsWrapper(user);
  }

  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    User user;
    if(emailValidator.validate(login)){
      user = userRepo.findByEmail(login)
          .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, login)));
    } else if (usernameValidator.validate(login)) {
      user = userRepo.findByUsername(login)
          .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, login)));
    } else {
      throw new UsernameNotFoundException(String.format(NOT_FOUND, login));
    }
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
    Role role = roleService.findRoleByRoleType(roleType);
    user.getRoles().remove(role);
    return new UserDetailsWrapper(user);
  }

  @Override
  public List<UserDetailsWrapper> findUsersByRole(AppRoleType roleType) {
    return userRepo.findAllByRole(roleType).stream()
        .map(k -> new UserDetailsWrapper(k))
        .collect(Collectors.toList());
  }

  //redundant, but avoid propagate class casting.
  @Override
  public Optional<UserDetailsWrapper> loadUserWrapperByUsername(String username) {
    Optional<UserDetailsWrapper> wrapper;
    try{
      wrapper = Optional.of((UserDetailsWrapper) loadUserByUsername(username));
    }catch(Exception ex){
      return Optional.empty();
    }
    return wrapper;
  }
}
