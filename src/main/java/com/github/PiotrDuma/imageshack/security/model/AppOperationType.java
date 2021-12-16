package com.github.PiotrDuma.imageshack.security.model;

import java.util.stream.Stream;

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

  public static Stream<AppOperationType> stream(){
    return Stream.of(AppOperationType.values());
  }
}
