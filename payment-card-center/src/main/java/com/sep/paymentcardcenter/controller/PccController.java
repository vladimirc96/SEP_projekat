package com.sep.paymentcardcenter.controller;

import com.sep.paymentcardcenter.dto.BankAccountDTO;
import com.sep.paymentcardcenter.dto.IssuerResponseDTO;
import com.sep.paymentcardcenter.dto.PaymentStatusDTO;
import com.sep.paymentcardcenter.dto.PccRequestDTO;
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
    private RestTemplate restTemplate;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/forward-payment/{paymentId}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IssuerResponseDTO> forward(@RequestBody PccRequestDTO pccRequestDTO, @PathVariable("paymentId") String id){

        if(!isValid(pccRequestDTO)){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        Transaction transaction = new Transaction();
        transaction.setTimestamp(new Date());
        transaction.setAmount(pccRequestDTO.getAmount());
        transaction.setPaymentStatus(pccRequestDTO.getPaymentStatus());
        transaction.setPaymentId(Long.parseLong(id));
        transaction = transactionService.save(transaction);

        // proslediti zahtev banci kupca
        HttpEntity<PccRequestDTO> entity = new HttpEntity<>(pccRequestDTO);
        ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.exchange("https://localhost:8451/bank/issuer/payment/" + id,
                HttpMethod.PUT, entity, IssuerResponseDTO.class);

        // sacuvaj odma stanje transakcije kad dobijes odg
        transaction = transactionService.update(responseEntity.getBody(), transaction);

        // responseEntity odgovor proslediti prodavcu
        return responseEntity;
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
