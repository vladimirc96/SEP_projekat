package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.PPClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientsRepository extends JpaRepository<PPClient, Long> {

    PPClient findOneById(Long id);
}
