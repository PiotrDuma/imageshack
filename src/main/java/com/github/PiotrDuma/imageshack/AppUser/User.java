package com.github.PiotrDuma.imageshack.AppUser;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private String username;

  @Email
  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull
  @NotEmpty
  private String password;
  private boolean enabled;
  private boolean blocked;

  public User() {
  }

  public User(String name, String email, String password, boolean enabled, boolean blocked) {
    this.username = name;
    this.email = email;
    this.password = password;
    this.enabled = enabled;
    this.blocked = blocked;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null; //TODO
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String name) {
    this.username = name;
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

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    this.Id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }
}
