package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.PPTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<PPTransaction, Long> {

    PPTransaction findOneByOrderId(String orderId);


    Optional<PPTransaction> findByActiveOrderId(Long activeOrderId);

    PPTransaction findOneByPaymentToken(String token);
}
