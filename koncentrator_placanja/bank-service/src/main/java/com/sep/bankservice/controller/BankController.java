package com.sep.bankservice.controller;

import com.sep.bankservice.dto.*;
import com.sep.bankservice.model.Customer;
import com.sep.bankservice.model.Transaction;
import com.sep.bankservice.service.CryptoService;
import com.sep.bankservice.service.CustomerService;
import com.sep.bankservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping(value="/bank")
public class BankController {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CryptoService cryptoService;

    // metoda koja prihvata zahtev za placanje i prosledjuje banci na proveru
    @RequestMapping(value = "/payment-request", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<RedirectDTO> payment(@RequestBody ActiveOrderDTO activeOrderDTO) {
        Customer customer = customerService.findOneById(activeOrderDTO.getSellerId());
        Transaction transaction = transactionService.create(activeOrderDTO, customer);

        String merchantPasswordDecrypted = cryptoService.decrypt(customer.getMerchantPassword());
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(customer.getMerchantId(),
                merchantPasswordDecrypted,
                transaction.getAmount(), transaction.getId(), transaction.getTimestamp());
        // poslati zahtev banci
        HttpEntity<PaymentRequestDTO> entity = new HttpEntity<>(paymentRequestDTO);
        ResponseEntity<RedirectDTO> responseEntity = restTemplate.exchange("https://localhost:8450/bank/check-payment-request", HttpMethod.PUT, entity, RedirectDTO.class);
        return responseEntity;
    }

    // belezi ishod transakcije u odnosu na to sta banka posalje
    @RequestMapping(value  = "/transaction/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateTransaction(@RequestBody PaymentStatusDTO paymentStatusDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(paymentStatusDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);
        System.out.println(transaction);
        return new ResponseEntity<>("Transakcija azurirana.", HttpStatus.OK);
    }

}
