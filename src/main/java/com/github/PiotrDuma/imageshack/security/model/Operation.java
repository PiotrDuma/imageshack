package com.github.PiotrDuma.imageshack.security.model;

import java.util.Collection;
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
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "operations")
public class Operation implements GrantedAuthority {

  @Id
  @Column(name = "operation_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "operation_type", unique = true)
  @Enumerated(EnumType.STRING)
  private AppOperationType operationType;

  @ManyToMany(mappedBy = "allowedOperations")
  private Collection<Role> roles;

  public Operation() {
  }

  public Operation(AppOperationType operationType) {
    this.operationType = operationType;
  }

  public Long getId() {
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
