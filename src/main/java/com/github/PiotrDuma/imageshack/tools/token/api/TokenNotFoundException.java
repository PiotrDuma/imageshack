package com.github.PiotrDuma.imageshack.tools.token.api;


import com.github.PiotrDuma.imageshack.exception.type.NotFoundException;

public class TokenNotFoundException extends NotFoundException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
