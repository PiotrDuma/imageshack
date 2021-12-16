package com.github.PiotrDuma.imageshack.security.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AppRoleRepo extends JpaRepository<Role, Long> {
//  @Query(value = "SELECT * FROM roles r WHERE r.role_type=1")
  Optional<Role> findRoleByRoleType(AppRoleType roleType);
}
