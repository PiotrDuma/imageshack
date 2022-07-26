package com.github.PiotrDuma.imageshack.AppUser.domain;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.AppRoleType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> getUserByUsername(@Param("username") String username);
    @Query("SELECT u FROM User u WHERE u.id= :userId")
    Optional<User> getUserById(@Param("userId") Long userId);
    //TODO: query by role type
    @Query("SELECT u FROM User u INNER JOIN u.roles r "
        + "WHERE r.roleType = :roleType")
    List<User> findAllByRole(@Param("roleType") AppRoleType roleType);
}
