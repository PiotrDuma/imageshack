package com.github.PiotrDuma.imageshack.security.config.SystemSecurityUserProvider;

import com.github.PiotrDuma.imageshack.AppUser.User;
import com.github.PiotrDuma.imageshack.AppUser.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.AppUser.UserRepository;
import com.github.PiotrDuma.imageshack.AppUser.UserService;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("AllRoles")
public class SystemSecurityDevUsersImpl implements SystemSecurityUserProvider {

  private final UserRepository repo;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public SystemSecurityDevUsersImpl(UserRepository repo, UserService userService,
         PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void generateSystemUsers() {

    if (repo.findByEmail("owner@imageshack.com").isEmpty()) {
      System.out.println("START CREATING SYSTEM OWNER");
      UserDetailsWrapper owner = userService.createNewUser("owner",
          "owner@imageshack.com", "passwd");
    }else{
      User owner = repo.findByEmail("owner@imageshack.com").get();
      userService.addRole(owner, AppRoleType.MODERATOR);

//      repo.save(owner.getUser());

//    User owner = new User("owner", "owner@imageshack.com", passwordEncoder.encode("passwd"));
//    Collection<Role> authorities = owner.getAuthorities();
//    authorities.add(roleRepo.findRoleByRoleType(AppRoleType.OWNER).get());
//    owner.setRoles(authorities);
//
//    if (repo.findByEmail(owner.getEmail()).isEmpty()) {
//      repo.save(owner);
    }
  }
}
