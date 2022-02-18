package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.security.model.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
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

  @ManyToMany(fetch = FetchType.EAGER) //TODO: https://stackoverflow.com/posts/37659432/revisions
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "user_id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "role_id"))
  private Collection<Role> roles = new HashSet<>();

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
  public Collection<Role> getAuthorities() {
    return roles; //TODO
  }

  public void setRoles(Collection<Role> roles){
    this.roles = roles;
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
