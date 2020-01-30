package com.sep.paypalservice.repository;

import com.sep.paypalservice.model.PPAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgreementRepository extends JpaRepository<PPAgreement, Long> {

    PPAgreement findOneById(Long id);

    PPAgreement findOneByTokenn(String tokenn);
}
