package com.sep.bankservice.controller;

import com.sep.bankservice.dto.PaymentDTO;
import com.sep.bankservice.dto.PaymentRequestDTO;
import com.sep.bankservice.model.Client;
import com.sep.bankservice.model.Transaction;
import com.sep.bankservice.service.ClientService;
import com.sep.bankservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
@RequestMapping(value="/bank")
public class BankController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    private ResponseEntity<?> payment(@RequestBody PaymentDTO paymentDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentDTO.getAmount());
        transaction.setTimestamp(new Date());

        Client client = clientService.findByMerchantId(paymentDTO.getMerchantId());

        transaction.setClient(client);
        transaction = transactionService.save(transaction);

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(client.getMerchanId(), client.getMerchantPassword(),
                paymentDTO.getAmount(), transaction.getId(), transaction.getTimestamp());

        // poslati zahtev banci

        return null;
    }


}
