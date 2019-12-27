package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.PPTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<PPTransaction, Long> {

}
