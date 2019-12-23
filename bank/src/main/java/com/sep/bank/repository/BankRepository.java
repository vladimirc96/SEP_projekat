package com.sep.bank.repository;

import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {

    Bank findOneById(Long id);
}
