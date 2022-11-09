package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import java.time.Instant;
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
    @Query("SELECT t FROM TokenAuth t WHERE t.expiredDateTime < :timeNow ")
    List<TokenAuth> getExpiredTokens(@Param("timeNow") Instant timeNow);
    @Query("SELECT t FROM TokenAuth t WHERE t.email = :email AND t.token = :value")
    Optional<TokenAuth> getTokenByEmailAndTokenValue(@Param("email") String email,
                                    @Param("value") String tokenValue);
    @Query("SELECT t FROM TokenAuth t WHERE t.email = :email AND t.token = :value AND t.tokenAuthType = :type")
    Optional<TokenAuth> getTokenByEmailAndValueAndType(@Param("email") String email,
        @Param("value") String tokenValue, @Param("type") TokenAuthType tokenType);
}
