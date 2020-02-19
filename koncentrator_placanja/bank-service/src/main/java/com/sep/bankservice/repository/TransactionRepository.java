package com.sep.bankservice.repository;

import com.sep.bankservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findOneById(Long id);
    Transaction findOneByPaymentId(Long id);
    Optional<Transaction> findByActiveOrderId(Long id);
}
