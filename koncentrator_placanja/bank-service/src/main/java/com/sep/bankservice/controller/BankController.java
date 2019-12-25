package com.sep.bankservice.controller;

import com.sep.bankservice.dto.PaymentDTO;
import com.sep.bankservice.dto.PaymentRequestDTO;
import com.sep.bankservice.dto.PaymentStatusDTO;
import com.sep.bankservice.dto.RedirectDTO;
import com.sep.bankservice.model.Customer;
import com.sep.bankservice.model.PaymentStatus;
import com.sep.bankservice.model.Transaction;
import com.sep.bankservice.service.CustomerService;
import com.sep.bankservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private CustomerService customerService;

    // metoda koja prihvata zahtev za placanje i prosledjuje banci na proveru
    @RequestMapping(value = "/payment-request", method = RequestMethod.POST)
        private ResponseEntity<RedirectDTO> payment(@RequestBody PaymentDTO paymentDTO) {
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentDTO.getAmount());
        transaction.setTimestamp(new Date());
        transaction.setPaymentStatus(PaymentStatus.PROCESSING);

        Customer customer = customerService.findByMerchantId(paymentDTO.getMerchantId());

        transaction.setCustomer(customer);
        transaction = transactionService.save(transaction);

        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(customer.getMerchantId(),
                customer.getMerchantPassword(),
                paymentDTO.getAmount(), transaction.getId(), transaction.getTimestamp());

        // poslati zahtev banci
        HttpEntity<PaymentRequestDTO> entity = new HttpEntity<>(paymentRequestDTO);
        ResponseEntity<RedirectDTO> responseEntity = restTemplate.exchange("https://localhost:8450/bank/check-payment-request", HttpMethod.PUT, entity, RedirectDTO.class);

        return responseEntity;
    }

    // belezi ishod transakcije u odnosu na to sta banka posalje
    @RequestMapping(value  = "/transaction/{id}", method = RequestMethod.PUT)
    private ResponseEntity<String> updateTransaction(@RequestBody PaymentStatusDTO paymentStatusDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(paymentStatusDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

}
