package org.sep.newtestservice.repository;

import org.sep.newtestservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByActiveOrderId(Long activeOrderId);
}
