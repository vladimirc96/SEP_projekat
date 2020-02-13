package com.sep.sellers.controller;

import com.sep.sellers.dto.KPRegistrationDTO;
import com.sep.sellers.dto.PaymentMethodDTO;
import com.sep.sellers.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {


    @Autowired
    PaymentMethodService _pmService;

    @PostMapping(value = "/")
    public @ResponseBody ResponseEntity createNewPaymentMethod(@RequestBody PaymentMethodDTO pmDTO) {
        return new ResponseEntity(_pmService.createNewPaymentMethod(pmDTO), HttpStatus.CREATED);
    }



}
