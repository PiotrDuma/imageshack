package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import com.github.PiotrDuma.imageshack.AppUser.domain.User;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
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
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority, Serializable {

  @Id
  @Column(name = "role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role_type", unique = true)
  @Enumerated(EnumType.STRING)
  private AppRoleType roleType;

  @ManyToMany(mappedBy = "roles")
  private Collection<User> users;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "roles_operations",
      joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "role_id"),
      inverseJoinColumns = @JoinColumn(
          name = "operation_id", referencedColumnName = "operation_id"))
  private Set<Operation> allowedOperations;

  protected Role() {
  }

  protected Role(AppRoleType roleType, Set<Operation> allowedOperations) {
    this.roleType = roleType;
    this.allowedOperations = allowedOperations;
  }

  protected Long getId() {
    return id;
  }

  @Override
  public String getAuthority() {
    return roleType.getRole();
  }

  public Set<? extends GrantedAuthority> getAllowedOperations() {
    return allowedOperations;
  }

  protected void setAllowedOperations(Set<Operation> allowedOperations) {
    this.allowedOperations = allowedOperations;
  }

  @Override
  public String toString() {
    return roleType.name();
  }


}
