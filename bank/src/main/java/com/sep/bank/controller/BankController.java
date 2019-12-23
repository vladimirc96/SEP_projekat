package com.sep.bank.controller;

import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.Client;
import com.sep.bank.service.BankService;
import com.sep.bank.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/bank")
public class BankController {

    private final String SUCCESS_URL = "";
    private final String FAILED_URL = "";
    private final String ERROR_URL = "";

    @Autowired
    private BankService bankService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public ResponseEntity<String> check(@RequestBody PaymentRequestDTO paymentRequest){
        if(!isRequestValid(paymentRequest)){
            return new ResponseEntity<>(FAILED_URL, HttpStatus.OK);
        }
        return new ResponseEntity<>(SUCCESS_URL, HttpStatus.OK);
    }

    private boolean isRequestValid(PaymentRequestDTO paymentRequest){
        Client client = clientService.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(), paymentRequest.getMerchantPassword());
        if(client == null){
            return false;
        }
        if(paymentRequest.getAmount() != 0){
            return false;
        }
//        else if(paymentRequest.getMerchantTimestamp()){
//
//        }
        return true;
    }


}
