package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.stream.Stream;

public enum AppOperationType {
  CREATE("OP_CREATE"),
  READ("OP_READ"),
  EDIT("OP_EDIT"),
  DELETE("OP_DELETE"),
  MODERATE("OP_MODERATE"),
  ADMINISTRATE("OP_ADMINISTRATE"),
  MANAGE("OP_MANAGE");


  private final String operation;

  AppOperationType(String operation) {
    this.operation = operation;
  }

  public String getOperationPermission() {
    return operation;
  }

  public static Stream<AppOperationType> stream(){
    return Stream.of(AppOperationType.values());
  }
}
