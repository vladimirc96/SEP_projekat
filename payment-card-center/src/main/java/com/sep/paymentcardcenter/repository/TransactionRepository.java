package com.sep.paymentcardcenter.repository;

import com.sep.paymentcardcenter.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findOneById(Long id);

}
