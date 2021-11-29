package com.github.PiotrDuma.imageshack.AppUser;

import java.util.Collection;
import javax.persistence.Entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {

  private String name;
  private String email;
  private String password;
  private boolean enabled;
  private boolean blocked;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null; //TODO
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return name;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !blocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}
