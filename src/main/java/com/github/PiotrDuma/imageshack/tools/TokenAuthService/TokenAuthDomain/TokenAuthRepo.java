package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain;

import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface TokenAuthRepo extends JpaRepository<TokenAuth, Long> {
    @Query("SELECT t FROM TokenAuth t")
    List<TokenAuth> getAllTokens();
    @Transactional
    void deleteByEmail(String email);
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
    @Query("SELECT t FROM TokenAuth t WHERE t.token = :value")
    Optional<TokenAuth> getTokenByValue(@Param("value") String tokenValue);
    @Modifying
    @Transactional
    @Query("DELETE FROM TokenAuth t WHERE t.expiredDateTime < :now")
    void deleteExpiredTokens(@Param("now") Instant now);
    @Modifying
    @Transactional
    @Query("DELETE FROM TokenAuth t WHERE t.expiredDateTime < :now AND t.email = :email")
    void deleteExpiredTokens(@Param("now") Instant now, @Param("email") String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM TokenAuth t WHERE t.expiredDateTime < :now AND t.tokenAuthType = :type")
    void deleteExpiredTokens(@Param("now") Instant now, @Param("type") TokenAuthType tokenType);
}
