package com.sep.bitcoinservice.repository;

import com.sep.bitcoinservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByOrderId(Long orderId);
    Optional<Transaction> findByActiveOrderId(Long activeOrderId);
    boolean existsByOrderId(Long id);

}
