package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.PPAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AgreementRepository extends JpaRepository<PPAgreement, Long> {

    PPAgreement findOneById(Long id);

    PPAgreement findOneByTokenn(String tokenn);

    @Query("select a from PPAgreement a where a.username = :username")
    List<PPAgreement> findAllByUsername(String username);
}
