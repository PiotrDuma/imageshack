package com.github.PiotrDuma.imageshack.AppUser;

import com.github.PiotrDuma.imageshack.AppUser.UserDetails.CustomUserDetails;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import com.github.PiotrDuma.imageshack.security.model.Role;
import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User implements Serializable { //TODO: custom implementation of database tables.

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long Id;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "details_id", referencedColumnName = "user_id")
  private CustomUserDetails customUserDetails;

  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String username;

  @Email
  @NotNull
  @NotEmpty
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull
  @NotEmpty
  private String password;

  @ManyToMany(fetch = FetchType.EAGER) //TODO: https://stackoverflow.com/posts/37659432/revisions
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "user_id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "role_id"))
  private Collection<Role> roles = new HashSet<>();

  protected User() {
  }

  protected User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    this.Id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String name) {
    this.username = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Collection<Role> getRoles() {
    return roles;
  }

  public void setRoles(Collection<Role> roles) {
    this.roles = roles;
  }

  public CustomUserDetails getCustomUserDetails() {
    return customUserDetails;
  }

  public void setCustomUserDetails(
      CustomUserDetails customUserDetails) {
    this.customUserDetails = customUserDetails;
  }
}
