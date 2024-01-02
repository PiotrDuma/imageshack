package com.github.PiotrDuma.imageshack.tools.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface TokenRepositoryJpa extends TokenRepository, JpaRepository<TokenEntity, Long> {
    @Query("SELECT t FROM TokenEntity t WHERE t.publicId = :publicId")
    Optional<TokenEntity> findByPublicId(@Param("publicId")UUID publicId);
}
