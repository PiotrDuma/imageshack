package com.github.PiotrDuma.imageshack.TokenGenerator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenGeneratorImplTest {
  private TokenGenerator generator;

  @BeforeEach
  void setUp(){
    this.generator = new TokenGeneratorImpl();
  }

  @Test
  void generateShouldReturn32CharString() {
    String result = this.generator.generate();

    assertEquals(32, result.length());
  }

  @Test
  void generateShouldReturnDifferentTokensAtTheTime(){
    String result1 = this.generator.generate();
    String result2 = this.generator.generate();

    assertFalse(result1.equals(result2));
  }
  
}