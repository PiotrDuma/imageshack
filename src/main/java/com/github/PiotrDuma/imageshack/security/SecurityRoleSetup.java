package com.github.PiotrDuma.imageshack.security;

import com.github.PiotrDuma.imageshack.security.model.AppOperationRepo;
import com.github.PiotrDuma.imageshack.security.model.AppOperationType;
import com.github.PiotrDuma.imageshack.security.model.AppRoleRepo;
import com.github.PiotrDuma.imageshack.security.model.AppRoleType;
import com.github.PiotrDuma.imageshack.security.model.Operation;
import com.github.PiotrDuma.imageshack.security.model.Role;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class SecurityRoleSetup {

  private final AppOperationRepo appOperationRepo;
  private final AppRoleRepo appRoleRepo;

  @Autowired
  public SecurityRoleSetup(AppOperationRepo appOperationRepo,
      AppRoleRepo appRoleRepo) {
    this.appOperationRepo = appOperationRepo;
    this.appRoleRepo = appRoleRepo;
  }

  //TODO: manage roles and privileges
  @Transactional
  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationEvent() {

    AppOperationType.stream().forEach(this::createOperation);

    Set<Operation> ownerAllowedOperations = Stream.of(AppOperationType.DELETE,
        AppOperationType.EDIT)
        .map(this::createOperation)
        .collect(Collectors.toSet());

    Set<Operation> adminAllowedOperations = Stream.of(AppOperationType.DELETE)
        .map(this::createOperation)
        .collect(Collectors.toSet());

    createRole(AppRoleType.OWNER, ownerAllowedOperations);

    //TODO: manage roles and privileges
    createRole(AppRoleType.ADMIN, adminAllowedOperations);
    createRole(AppRoleType.MODERATOR, null);
    createRole(AppRoleType.USER, null);
  }

  @Transactional
  public Operation createOperation(AppOperationType operationType) {
    Optional<Operation> op = appOperationRepo.findOperationByOperationType(operationType);
    if (op.isEmpty()) {
      return appOperationRepo.save(new Operation(operationType));
    }
    return op.get();
  }

  @Transactional
  public Role createRole(AppRoleType roleType, Set<Operation> allowedOperations) {
    Optional<Role> role = appRoleRepo.findRoleByRoleType(roleType);
    if (role.isEmpty()) {
      return appRoleRepo.save(new Role(roleType, allowedOperations));
    }
    return role.get();
  }

}
