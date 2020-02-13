package com.sep.paymentcardcenter.controller;

import com.sep.paymentcardcenter.client.BankClient;
import com.sep.paymentcardcenter.dto.BankAccountDTO;
import com.sep.paymentcardcenter.dto.IssuerResponseDTO;
import com.sep.paymentcardcenter.dto.PaymentStatusDTO;
import com.sep.paymentcardcenter.dto.PccRequestDTO;
import com.sep.paymentcardcenter.model.PaymentStatus;
import com.sep.paymentcardcenter.model.Transaction;
import com.sep.paymentcardcenter.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
@RequestMapping(value = "/pcc")
public class PccController {

    @Autowired
    private BankClient bankClient;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/forward-payment/{paymentId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IssuerResponseDTO> forward(@RequestBody PccRequestDTO pccRequestDTO, @PathVariable("paymentId") String id){

        if(!isValid(pccRequestDTO)){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Transaction transaction = transactionService.create(pccRequestDTO, Long.parseLong(id));
        ResponseEntity<IssuerResponseDTO> responseEntity = bankClient.forward(pccRequestDTO, transaction, id);
        // sacuvaj odma stanje transakcije kad dobijes odg
        transaction = transactionService.update(responseEntity.getBody(), transaction);
        return responseEntity;
    }

    @RequestMapping(value = "/transaction-failed/{pccOrderId}", method = RequestMethod.PUT, consumes = "application/json", produces = "applciation/json")
    private ResponseEntity<String> update(@PathVariable("pccOrderId") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(PaymentStatus.FAILURE);
        transaction = transactionService.save(transaction);
        return new ResponseEntity("Updated", HttpStatus.OK);
    }

    private boolean isValid(PccRequestDTO pccRequestDTO){
        BankAccountDTO bankAccountDTO = pccRequestDTO.getBankAccountDTO();
        if(pccRequestDTO.getAcquirerOrderId() == 0){
            return false;
        }
        if(pccRequestDTO.getAcquirerTimepstamp() == null){
            return false;
        }

        if(bankAccountDTO.getCardholderName().equals("")){
            return false;
        }else if(bankAccountDTO.getExpirationDate() == null){
            return false;
        }else if(bankAccountDTO.getPan().equals("")){
            return false;
        }else if(bankAccountDTO.getServiceCode().equals("")){
            return false;
        }

        return true;
    }

}
