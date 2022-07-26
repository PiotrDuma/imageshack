package com.github.PiotrDuma.imageshack.AppUser.domain;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsWrapper implements UserDetails {
  private User user;
  private CustomUserDetails customUserDetails;

  public UserDetailsWrapper(User user) {
    this.user = user;
    this.customUserDetails = user.getCustomUserDetails();
  }

  protected UserDetailsWrapper(Builder builder){
    this.user = new User(builder.user.getUsername(),
        builder.user.getEmail(),
        builder.user.getPassword());
    this.customUserDetails = new CustomUserDetails(
        builder.customUserDetails.isAccountNonExpired(),
        builder.customUserDetails.isAccountNonLocked(),
        builder.customUserDetails.isCredentialsNonExpired(),
        builder.customUserDetails.isEnabled()
    );
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> authorities =
        user.getRoles().stream().flatMap(role -> role.getAllowedOperations().stream()).collect(Collectors.toSet());

    user.getRoles().stream()
        .forEach(r -> authorities.add(r));
    return authorities;
  }
  public Long getUserId(){
    return this.user.getId();
  }

  protected User getUser(){
    return this.user;
  }

  protected CustomUserDetails getCustomUserDetails(){
    return this.customUserDetails;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  public void setPassword(String password) {
    user.setPassword(password);
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public void setUsername(String username) {
    user.setPassword(username);
  }

  void setUserEmail(String email){
    this.user.setEmail(email);
  }

  String getEmail(){
    return this.user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return customUserDetails.isAccountNonExpired();
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.customUserDetails.setAccountNonExpired(accountNonExpired);
  }

  @Override
  public boolean isAccountNonLocked() {
    return customUserDetails.isAccountNonLocked();
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.customUserDetails.setAccountNonLocked(accountNonLocked);
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return customUserDetails.isCredentialsNonExpired();
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.customUserDetails.setCredentialsNonExpired(credentialsNonExpired);
  }

  @Override
  public boolean isEnabled() {
    return customUserDetails.isEnabled();
  }

  public void setEnabled(boolean enabled) {
    this.customUserDetails.setEnabled(enabled);
  }

  public static class Builder{
    private User user;
    private CustomUserDetails customUserDetails;

    public Builder(String username, String email, String password){
      this.user = new User(username, email, password);
      this.customUserDetails = new CustomUserDetails(true, true,true, true);
      this.customUserDetails.setUser(user);
      this.user.setCustomUserDetails(this.customUserDetails);
    }

    public Builder accountNonExpired(boolean accountNonExpired) {
      this.customUserDetails.setAccountNonExpired(accountNonExpired);
      return this;
    }

    public Builder accountNonLocked(boolean accountNonLocked) {
      this.customUserDetails.setAccountNonLocked(accountNonLocked);
      return this;
    }

    public Builder credentialsNonExpired(boolean credentialsNonExpired) {
      this.customUserDetails.setCredentialsNonExpired(credentialsNonExpired);
      return this;
    }

    public Builder enabled(boolean enabled) {
      this.customUserDetails.setEnabled(enabled);
      return this;
    }

    public UserDetailsWrapper build(){
      return new UserDetailsWrapper(this);
    }
  }
}
