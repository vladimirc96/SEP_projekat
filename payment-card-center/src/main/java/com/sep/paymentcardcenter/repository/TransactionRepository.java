package com.sep.paymentcardcenter.repository;

import com.sep.paymentcardcenter.model.PaymentStatus;
import com.sep.paymentcardcenter.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findOneById(Long id);
    Transaction findOneByPaymentId(Long id);
    List<Transaction> findAllByPaymentStatus(PaymentStatus paymentStatus);

}
