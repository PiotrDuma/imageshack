package com.github.PiotrDuma.imageshack.security.config.SystemSecurityUserProvider;

import com.github.PiotrDuma.imageshack.AppUser.User;
import com.github.PiotrDuma.imageshack.AppUser.UserRepository;
import com.github.PiotrDuma.imageshack.security.model.AppRoleRepo;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import com.github.PiotrDuma.imageshack.security.model.Role;
import java.util.Collection;
import javax.swing.JPasswordField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("AllRoles")
public class SystemSecurityDevUsersImpl implements SystemSecurityUserProvider {

  private final UserRepository repo;
  private final AppRoleRepo roleRepo;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public SystemSecurityDevUsersImpl(UserRepository repo, AppRoleRepo roleRepo,
         PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.roleRepo = roleRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void generateSystemUsers() {

    User owner = new User("owner", "owner@imageshack.com", passwordEncoder.encode("passwd"), true, false);
    Collection<Role> authorities = owner.getAuthorities();
    authorities.add(roleRepo.findRoleByRoleType(AppRoleType.OWNER).get());
    owner.setRoles(authorities);

    if (repo.findByEmail(owner.getEmail()).isEmpty()) {
      repo.save(owner);
    }
  }
}
