package com.github.PiotrDuma.imageshack.security.model;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority {

  @Id
  @Enumerated(EnumType.STRING)
  private AppRoleType id;

  @OneToMany
  private Collection<Operation> allowedOperations;

  public Role() {
  }

  public Role(AppRoleType id, Collection<Operation> allowedOperations) {
    this.id = id;
    this.allowedOperations = allowedOperations;
  }

  @Override
  public String getAuthority() {
    return id.name();
  }

  public Collection<Operation> getAllowedOperations() {
    return allowedOperations;
  }
}
