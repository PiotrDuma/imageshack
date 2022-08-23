package com.github.PiotrDuma.imageshack.AppUser.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_details")
class CustomUserDetails implements Serializable {

  @Id
  @Column(name = "details_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;

  public CustomUserDetails() {
  }

  public CustomUserDetails(boolean accountNonExpired, boolean accountNonLocked,
      boolean credentialsNonExpired, boolean enabled) {
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id){
    this.id = id;
  }

  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "CustomUserDetails{" +
        "  user=" + user +
        ", accountNonExpired=" + accountNonExpired +
        ", accountNonLocked=" + accountNonLocked +
        ", credentialsNonExpired=" + credentialsNonExpired +
        ", enabled=" + enabled +
        '}';
  }
}
