package com.sep.bankservice.repository;

import com.sep.bankservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findOneById(Long id);
}
