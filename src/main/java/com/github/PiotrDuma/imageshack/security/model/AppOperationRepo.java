package com.github.PiotrDuma.imageshack.security.model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AppOperationRepo extends JpaRepository<Operation, String> {
  Optional<Operation> findOperationById(String id);
}
