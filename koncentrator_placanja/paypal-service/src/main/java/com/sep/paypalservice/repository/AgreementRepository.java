package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.PPAgreement;
import com.sep.paypalservice.model.PPTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AgreementRepository extends JpaRepository<PPAgreement, Long> {

    PPAgreement findOneById(Long id);

    PPAgreement findOneByTokenn(String tokenn);


    Optional<PPAgreement> findByActiveOrderId(Long activeOrderId);

    @Query("select a from PPAgreement a where a.username = :username")
    List<PPAgreement> findAllByUsername(String username);
}
