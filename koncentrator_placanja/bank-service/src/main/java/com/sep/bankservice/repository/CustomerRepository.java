package com.sep.bankservice.repository;

import com.sep.bankservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findOneById(Long id);
    Customer findByMerchantId(String id);

}
