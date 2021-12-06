package com.github.PiotrDuma.imageshack.security.model;

import org.springframework.security.core.GrantedAuthority;

public enum AppOperationType { //TODO add new privileges
  DELETE("OP_DELETE"),
  EDIT("OP_EDIT");

  private final String operation;

  AppOperationType(String operation) {
    this.operation = operation;
  }

  public String getOperationPermission() {
    return operation;
  }


}
