package com.sep.bank.repository;

import com.sep.bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findOneById(Long id);
    Customer findByMerchantId(String id);
    Customer findByMerchantIdAndMerchantPassword(String id, String password);
    // Customer findOneByPan(String pan);
}
