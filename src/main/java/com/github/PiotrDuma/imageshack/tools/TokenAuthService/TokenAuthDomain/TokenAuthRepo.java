package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface TokenAuthRepo extends JpaRepository<TokenAuth, Long> {

}
