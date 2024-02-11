package com.github.PiotrDuma.imageshack.tools.token.api;

public class TokenProvider {
    private final Long ownerId;
    private final TokenType tokenType;
    private final int tokenActiveTimeMinutes;

    private TokenProvider(TokenBuilder tokenBuilder) {
        this.ownerId = tokenBuilder.ownerId;
        this.tokenType = tokenBuilder.tokenType;
        this.tokenActiveTimeMinutes = tokenBuilder.tokenActiveTimeMinutes;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getTokenActiveTimeMinutes() {
        return tokenActiveTimeMinutes;
    }

    public static class TokenBuilder {
        private final Long ownerId;
        private TokenType tokenType = TokenType.AUTHENTICATION;
        private int tokenActiveTimeMinutes = 60;

        public TokenBuilder(Long ownerId) {
            this.ownerId = ownerId;
        }

        public TokenBuilder setTokenType(TokenType tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public TokenBuilder setTokenActiveTimeMinutes(int minutes) {
            this.tokenActiveTimeMinutes = minutes;
            return this;
        }

        public TokenProvider build() {
            return new TokenProvider(this);
        }
    }
}
