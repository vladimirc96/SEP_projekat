package com.sep.bankservice.repository;

import com.sep.bankservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findOneById(Long id);
    Client findByMerchantId(String id);

}
