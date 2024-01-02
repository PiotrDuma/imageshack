package com.github.PiotrDuma.imageshack.tools.token.api;

import java.util.UUID;

public class TokenRequest {
    private final UUID tokenId;
    private final UUID value;
    private final TokenType tokenType;

    public TokenRequest(UUID tokenId, UUID value, TokenType tokenType) {
        this.tokenId = tokenId;
        this.value = value;
        this.tokenType = tokenType;
    }

    public UUID getTokenId() {
        return tokenId;
    }

    public UUID getValue() {
        return value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}
