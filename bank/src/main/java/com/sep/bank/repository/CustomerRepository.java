package com.sep.bank.repository;

import com.sep.bank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findOneById(Long id);
    Customer findByMerchantId(String id);
    Customer findByMerchantIdAndMerchantPassword(String id, String password);

    @Query("select customer from Customer customer where customer.bankAccount.pan  = :pan")
    Customer findOneByPan(@Param("pan") String pan);
}
