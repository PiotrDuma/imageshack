package com.github.PiotrDuma.imageshack.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class PasswordEncoderConfig {

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(8);
  }
}
