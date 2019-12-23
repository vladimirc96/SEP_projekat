package com.sep.bank.controller;

import com.sep.bank.dto.BankAccountDTO;
import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Customer;
import com.sep.bank.service.BankAccountService;
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

    private final String SUCCESS_URL = "SUCCESS";
    private final String FAILED_URL = "FAILED";
    private final String ERROR_URL = "";

    @Autowired
    private BankService bankService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT)
    public ResponseEntity<String> check(@RequestBody PaymentRequestDTO paymentRequest){
        if(!isRequestValid(paymentRequest)){
            return new ResponseEntity<>(FAILED_URL, HttpStatus.OK);
        }
        return new ResponseEntity<>(SUCCESS_URL, HttpStatus.OK);
    }


    @RequestMapping(value = "/payment", method = RequestMethod.PUT)
    public ResponseEntity<String> payment(@RequestBody BankAccountDTO bankAccountDTO){
        BankAccount bankAccount = bankAccountService.validate(bankAccountDTO);
        if(bankAccount == null){

        }



    }


    private boolean isRequestValid(PaymentRequestDTO paymentRequest){
        Customer customer = clientService.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(), paymentRequest.getMerchantPassword());
        if(customer == null){
            return false;
        }
        if(paymentRequest.getAmount() == 0){
            return false;
        }else if(paymentRequest.getMerchantTimestamp() == null){
            return false;
        }
        return true;
    }


}
