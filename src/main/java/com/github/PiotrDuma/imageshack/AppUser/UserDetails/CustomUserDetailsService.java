package com.github.PiotrDuma.imageshack.AppUser.UserDetails;

import com.github.PiotrDuma.imageshack.AppUser.User;
import com.github.PiotrDuma.imageshack.AppUser.UserDetailsWrapper;
import com.github.PiotrDuma.imageshack.AppUser.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  private final static String NOT_FOUND = "USER %s NOT FOUND";
  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException(String.format(NOT_FOUND, email)));
    return new UserDetailsWrapper(user);
  }


}
