package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
class SecurityRoleConfigurationImpl implements SecurityRoleConfiguration {
  private final AppOperationRepo appOperationRepo;
  private final AppRoleRepo appRoleRepo;

  @Autowired
  public SecurityRoleConfigurationImpl(AppOperationRepo appOperationRepo,
                    AppRoleRepo appRoleRepo) {
    this.appOperationRepo = appOperationRepo;
    this.appRoleRepo = appRoleRepo;
  }

  @Transactional
  public void setupSecurityRoleConfiguration() {
    AppOperationType.stream().forEach(this::createOperation);

    Set<Operation> ownerAllowedOperations = Stream.of(AppOperationType.MANAGE
        )
        .map(this::createOperation)
        .collect(Collectors.toSet());

    Set<Operation> adminAllowedOperations = Stream.of(
            AppOperationType.ADMINISTRATE)
        .map(this::createOperation)
        .collect(Collectors.toSet());

    Set<Operation> moderatorAllowedOperations = Stream.of(
            AppOperationType.MODERATE)
        .map(this::createOperation)
        .collect(Collectors.toSet());

    Set<Operation> userAllowedOperations = Stream.of(
            AppOperationType.CREATE,
            AppOperationType.READ,
            AppOperationType.EDIT,
            AppOperationType.DELETE)
        .map(this::createOperation)
        .collect(Collectors.toSet());

    createRole(AppRoleType.OWNER, ownerAllowedOperations);
    createRole(AppRoleType.ADMIN, adminAllowedOperations);
    createRole(AppRoleType.MODERATOR, moderatorAllowedOperations);
    createRole(AppRoleType.USER, userAllowedOperations);
  }

  @Transactional
  private Operation createOperation(AppOperationType operationType) {
    Optional<Operation> op = appOperationRepo.findOperationByOperationType(operationType);
    if (op.isEmpty()) {
      return appOperationRepo.save(new Operation(operationType));
    }
    return op.get();
  }

  @Transactional
  private Role createRole(AppRoleType roleType, Set<Operation> allowedOperations) {
    Optional<Role> role = appRoleRepo.findRoleByRoleType(roleType);
    if (role.isEmpty()) {
      return appRoleRepo.save(new Role(roleType, allowedOperations));
    }
    return role.get();
  }
}
