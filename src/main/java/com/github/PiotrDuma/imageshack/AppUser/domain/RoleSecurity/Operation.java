package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "operations")
public class Operation implements GrantedAuthority, Serializable {

  @Id
  @Column(name = "operation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "operation_type", unique = true)
  @Enumerated(EnumType.STRING)
  private AppOperationType operationType;

  @ManyToMany(mappedBy = "allowedOperations")
  private Collection<Role> roleEntities;

  protected Operation() {
  }

  protected Operation(AppOperationType operationType) {
    this.operationType = operationType;
  }

  protected Long getId() {
    return id;
  }

  @Override
  public String getAuthority() {
    return operationType.getOperationPermission();
  }

  @Override
  public String toString() {
    return this.operationType.name();
  }
}
