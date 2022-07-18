package com.github.PiotrDuma.imageshack.AppUser.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface UserRepository extends JpaRepository<User, UserId> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> getUserByUsername(@Param("username") String username);
    @Query("SELECT u FROM User u WHERE u.id.id= :userId")
    Optional<User> getUserById(@Param("userId") Long userId);
}
