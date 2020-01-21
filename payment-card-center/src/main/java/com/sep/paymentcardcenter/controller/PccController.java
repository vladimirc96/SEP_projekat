package com.sep.paymentcardcenter.controller;

import com.sep.paymentcardcenter.dto.BankAccountDTO;
import com.sep.paymentcardcenter.dto.IssuerResponseDTO;
import com.sep.paymentcardcenter.dto.PaymentStatusDTO;
import com.sep.paymentcardcenter.dto.PccRequestDTO;
import com.sep.paymentcardcenter.model.Transaction;
import com.sep.paymentcardcenter.service.TransactionService;
import org.hibernate.boot.model.source.spi.IdentifierSourceSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;

@RestController
@RequestMapping(value = "/pcc")
public class PccController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/forward-payment", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> forward(@RequestBody PccRequestDTO pccRequestDTO){

        if(!isValid(pccRequestDTO)){
            return new ResponseEntity<>("Neuspesno", HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction();
        transaction.setId(pccRequestDTO.getAcquirerOrderId());
        transaction.setTimestamp(pccRequestDTO.getAcquirerTimepstamp());
        transaction.setAmount(pccRequestDTO.getAmount());
        transaction.setPaymentStatus(pccRequestDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);

        // proslediti zahtev banci kupca
        HttpEntity<PccRequestDTO> entity = new HttpEntity<>(pccRequestDTO);
        ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.exchange("https://localhost:8451/bank/issuer/validate/" + pccRequestDTO.getAcquirerOrderId(),
                HttpMethod.PUT, entity, IssuerResponseDTO.class);

        // responseEntity odgovor proslediti prodavcu
        return responseEntity;
    }

    // belezi ishod transakcije u odnosu na to sta banka posalje
    @RequestMapping(value  = "/transaction/{id}", method = RequestMethod.PUT)
    private ResponseEntity<String> updateTransaction(@RequestBody PaymentStatusDTO paymentStatusDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(paymentStatusDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);
        System.out.println(transaction);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
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
