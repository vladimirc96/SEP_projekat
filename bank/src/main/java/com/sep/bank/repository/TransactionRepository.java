package com.sep.bank.repository;

import com.sep.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findOneById(Long id);
    Transaction findOneByPaymentId(Long id);
}
