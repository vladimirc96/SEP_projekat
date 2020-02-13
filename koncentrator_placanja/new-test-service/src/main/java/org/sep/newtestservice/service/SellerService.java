package org.sep.newtestservice.service;

import org.sep.newtestservice.client.RegistrationClient;
import org.sep.newtestservice.dto.SellerDTO;
import org.sep.newtestservice.model.Seller;
import org.sep.newtestservice.model.Transaction;
import org.sep.newtestservice.repository.SellerRepository;
import org.sep.newtestservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepo;

    @Autowired
    TransactionRepository transactionRepo;

    @Autowired
    RegistrationClient registrationClient;


    @Transactional(rollbackFor = AccessDeniedException.class)
    public SellerDTO registerSeller(SellerDTO sDTO) throws AccessDeniedException {

        Seller s = new Seller();

        s.setId(sDTO.getId());
        s.setSomeToken(sDTO.getSomeToken());

        s = sellerRepo.save(s);

        registrationClient.approveRegistration(sDTO);

        return SellerDTO.formDto(s);
    }
}
