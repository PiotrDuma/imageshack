package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class RoleServiceImpl implements RoleService{
  private final static String NOT_FOUND = "ROLE %s NOT FOUND";
  private final AppRoleRepo repo;

  @Autowired
  protected RoleServiceImpl(AppRoleRepo repo) {
    this.repo = repo;
  }
  @Override
  public Role findRoleByRoleType(AppRoleType roleType) throws NoSuchElementException {
    return repo.findRoleByRoleType(roleType).orElseThrow(
        () -> new NoSuchRoleException(String.format(NOT_FOUND, roleType))
    );
  }
}
