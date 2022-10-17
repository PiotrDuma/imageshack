package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenAuth;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface TokenAuthRepo extends JpaRepository<TokenAuth, Long> {
    Optional<TokenAuth> getTokenByEmail(String email);
    List<TokenAuth> getTokensByEmail(String email);
    @Query("SELECT t FROM TokenAuth t WHERE t.expired < :timeNow ")
    Stream<TokenAuth> getExpiredTokens(@Param("timeNow") Instant timeNow);
}
