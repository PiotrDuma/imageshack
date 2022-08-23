package com.github.PiotrDuma.imageshack.AppUser.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserDetailsRepository extends JpaRepository<CustomUserDetails, Long> {

}
