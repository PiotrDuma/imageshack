package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.Optional;

public interface RoleService {
  Role findRoleByRoleType(AppRoleType roleType) throws NoSuchRoleException;
}
