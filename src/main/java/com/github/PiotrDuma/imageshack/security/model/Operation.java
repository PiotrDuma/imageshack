package com.github.PiotrDuma.imageshack.security.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Operation implements GrantedAuthority {

  @Id
  @Enumerated(EnumType.STRING)
  private Enum<AppOperationType> id;

  public Operation() {
  }

  public Operation(AppOperationType id) {
    this.id = id;
  }

  @Override
  public String getAuthority() {
    return id.name();
  }

}
