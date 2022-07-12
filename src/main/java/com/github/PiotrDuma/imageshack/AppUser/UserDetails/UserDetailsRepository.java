package com.github.PiotrDuma.imageshack.AppUser.UserDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<CustomUserDetails, Long> {

}
