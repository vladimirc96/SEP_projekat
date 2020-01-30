package com.sep.bank.repository;

import com.sep.bank.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findOneById(Long id);

}
