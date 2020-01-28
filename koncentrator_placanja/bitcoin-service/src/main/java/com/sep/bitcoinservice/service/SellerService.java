package com.sep.bitcoinservice.service;

import com.sep.bitcoinservice.client.RegistrationClient;
import com.sep.bitcoinservice.dto.SellerDTO;
import com.sep.bitcoinservice.model.Seller;
import com.sep.bitcoinservice.repository.SellerRepository;
import com.sep.bitcoinservice.service.contracts.ISellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
public class SellerService implements ISellerService {

    @Autowired
    SellerRepository _sellerRepo;

    @Autowired
    RegistrationClient registrationClient;

    @Autowired
    CryptoService cryptoService;

    @Override
    @Transactional(rollbackFor = AccessDeniedException.class)
    public SellerDTO registerSeller(SellerDTO sDTO) throws AccessDeniedException {

        Seller s = new Seller();

        s.setId(sDTO.getId());
        s.setAuthToken(cryptoService.encrypt(sDTO.getAuthToken()));

        s = _sellerRepo.save(s);

        registrationClient.approveRegistration(sDTO);

        return SellerDTO.formDto(s);
    }


}
