package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AppRoleRepo extends JpaRepository<Role, Long> {
//  @Query(value = "SELECT * FROM roles r WHERE r.role_type=1")
  Optional<Role> findRoleByRoleType(AppRoleType roleType);
}
