package com.sep.sellers.service;

import com.sep.sellers.dto.PaymentMethodDTO;
import com.sep.sellers.model.PaymentMethod;
import com.sep.sellers.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentMethodService {

    @Autowired
    PaymentMethodRepository _pmRepo;

    @Autowired
    SellerService _sellerService;

    public long createNewPaymentMethod(PaymentMethodDTO pmDTO) {
        PaymentMethod pm = new PaymentMethod();

        pm.setName(pmDTO.getName());
        pm.setServiceBaseUrl(pmDTO.getServiceBaseUrl());
        pm.setRegistrationUrl(pmDTO.getRegistrationLink());

        pm = _pmRepo.save(pm);

        _sellerService.registerNewPaymentService(pm);

        return pm.getId();
    }
}
