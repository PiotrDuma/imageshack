package com.github.PiotrDuma.imageshack.security.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AppOperationRepo extends JpaRepository<Operation, Long> {
  Optional<Operation> findOperationById(String id);
//  @Query(value = "SELECT * FROM operations o WHERE o.operation_type=1",
//          nativeQuery = true)
  Optional<Operation> findOperationByOperationType(AppOperationType operationType);
}
