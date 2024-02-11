package com.github.PiotrDuma.imageshack.tools.token;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {
    TokenEntity save(TokenEntity token);
    Optional<TokenEntity> findByPublicId(UUID publicId);
}
