package com.sep.bank.repository;

import com.sep.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findOneById(Long id);
    Client findByMerchantId(String id);
    Client findByMerchantIdAndMerchantPassword(String id, String password);
}
