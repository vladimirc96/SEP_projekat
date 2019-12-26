package com.sep.sellers.service;

import com.sep.sellers.dto.SellerDTO;
import com.sep.sellers.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    SellerRepository sellerRepo;

    public SellerDTO getSeller(long id) {
        return SellerDTO.formDto(sellerRepo.findById(id).get());
    }
}
