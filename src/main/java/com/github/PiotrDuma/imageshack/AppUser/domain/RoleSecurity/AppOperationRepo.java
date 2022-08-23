package com.github.PiotrDuma.imageshack.AppUser.domain.RoleSecurity;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AppOperationRepo extends JpaRepository<Operation, Long> {
  Optional<Operation> findOperationById(String id);
//  @Query(value = "SELECT * FROM operations o WHERE o.operation_type=1",
//          nativeQuery = true)
  Optional<Operation> findOperationByOperationType(AppOperationType operationType);
}
