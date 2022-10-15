package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface TokenAuthRepo extends JpaRepository<TokenAuth, Long> {
    Optional<TokenAuth> getTokenByEmail(String email);
    List<TokenAuth> getTokensByEmail(String email);
}
