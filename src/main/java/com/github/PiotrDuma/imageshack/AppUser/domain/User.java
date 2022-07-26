package com.github.PiotrDuma.imageshack.AppUser.domain;

import com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity.Role;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  private CustomUserDetails customUserDetails;

  @NotNull
  @NotEmpty
  @Column(name = "username" ,nullable = false, unique = true)
  private String username;

  @Email
  @NotNull
  @NotEmpty
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @NotNull
  @NotEmpty
  @Column(name = "password", nullable = false) //TODO: requirements, password strength
  private String password;

  @ManyToMany(fetch = FetchType.EAGER) //TODO: https://stackoverflow.com/posts/37659432/revisions
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "user_id"),
      inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "role_id"))
  private Set<Role> roles = new HashSet<>();

  protected User() {
  }

  protected User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long userId) {
    this.id = userId;
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public CustomUserDetails getCustomUserDetails() {
    return customUserDetails;
  }

  public void setCustomUserDetails(
      CustomUserDetails customUserDetails) {
    this.customUserDetails = customUserDetails;
  }

  @Override
  public String toString() {
    return "User{" +
        "Id=" + id +
        ", username='" + username + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", roles=" + roles +
        '}';
  }
}
