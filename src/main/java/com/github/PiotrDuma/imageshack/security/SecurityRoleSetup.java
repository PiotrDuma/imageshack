package com.github.PiotrDuma.imageshack.security;

import com.github.PiotrDuma.imageshack.security.model.AppOperationRepo;
import com.github.PiotrDuma.imageshack.security.model.AppOperationType;
import com.github.PiotrDuma.imageshack.security.model.AppRoleRepo;
import com.github.PiotrDuma.imageshack.security.model.Operation;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

//TODO end up this class
public class SecurityRoleSetup {

  private final AppOperationRepo appOperationRepo;
  private final AppRoleRepo appRoleRepo;

  @Autowired
  public SecurityRoleSetup(AppOperationRepo appOperationRepo,
      AppRoleRepo appRoleRepo) {
    this.appOperationRepo = appOperationRepo;
    this.appRoleRepo = appRoleRepo;
  }

  @Transactional
  public Operation createOperation(AppOperationType operation) {
    Optional<Operation> op = appOperationRepo.findOperationById(operation.name());
    if (op.isEmpty()) {
      return appOperationRepo.save(new Operation(operation));
    }
    return op.get();
  }

//  @Transactional
//  public Role createRole(){
//}

}
