package com.github.PiotrDuma.imageshack.tools.token;

import java.util.Optional;
import java.util.UUID;

interface TokenRepository {
    TokenEntity save(TokenEntity token);
    Optional<TokenEntity> findByPublicId(UUID publicId);
}
