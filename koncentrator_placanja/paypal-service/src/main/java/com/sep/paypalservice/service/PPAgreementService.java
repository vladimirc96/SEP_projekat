package com.sep.paypalservice.service;

import com.sep.paypalservice.model.PPAgreement;
import com.sep.paypalservice.repository.AgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PPAgreementService {

    @Autowired
    AgreementRepository agreementRepository;

    public List<PPAgreement> findAll() {
        return agreementRepository.findAll();
    }

    public PPAgreement findOneById(Long id) {
        return agreementRepository.findOneById(id);
    }

    public PPAgreement save(PPAgreement agr) {
        return agreementRepository.save(agr);
    }

    public void remove(Long id) {
        agreementRepository.deleteById(id);
    }

    public PPAgreement findOneByTokenn(String tokenn) {
        return agreementRepository.findOneByTokenn(tokenn);
    }
}
