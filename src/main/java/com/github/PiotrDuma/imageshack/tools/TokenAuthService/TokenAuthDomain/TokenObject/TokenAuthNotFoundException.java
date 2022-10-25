package com.github.PiotrDuma.imageshack.tools.TokenAuthService.TokenAuthDomain.TokenObject;


public class TokenAuthNotFoundException extends RuntimeException {
    public TokenAuthNotFoundException() {}
    public TokenAuthNotFoundException(String message) {
        super(message);
    }
}
