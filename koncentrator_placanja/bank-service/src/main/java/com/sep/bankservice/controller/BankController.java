package com.sep.bankservice.controller;

import com.sep.bankservice.client.BankClient;
import com.sep.bankservice.client.SellerClient;
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

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value="/bank")
public class BankController {


    @Autowired
    private BankClient bankClient;

    @Autowired
    private SellerClient sellerClient;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CryptoService cryptoService;

    // metoda koja prihvata zahtev za placanje i prosledjuje banci na proveru
    @RequestMapping(value = "/payment-request", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<RedirectDTO> payment(@RequestBody ActiveOrderDTO activeOrderDTO, HttpServletRequest request) {
        Customer customer = customerService.findOneById(activeOrderDTO.getSellerId());
        Transaction transaction = transactionService.create(activeOrderDTO, customer);

        String merchantPasswordDecrypted = cryptoService.decrypt(customer.getMerchantPassword());
        ResponseEntity<String> baseUrl = sellerClient.getBaseUrl(activeOrderDTO.getSellerId());
        ResponseEntity<RedirectDTO> responseEntity = bankClient.forwardPaymentRequest(transaction, customer, merchantPasswordDecrypted, baseUrl.getBody());
        transaction = transactionService.setPaymentId(transaction, responseEntity.getBody().getPaymentId());
        return responseEntity;
    }


    // belezi ishod transakcije u odnosu na to sta banka posalje
    @RequestMapping(value  = "/transaction", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateTransaction(@RequestBody PaymentStatusDTO paymentStatusDTO){
        Transaction transaction = transactionService.findOneById(paymentStatusDTO.getId());
        transaction.setPaymentStatus(paymentStatusDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);
        return new ResponseEntity<>("Transakcija azurirana.", HttpStatus.OK);
    }

    @RequestMapping(value = "/get-base-url/{paymentId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> getBaseUrl(@PathVariable("paymentId") String paymentId){
        Transaction transaction = transactionService.findOneByPaymentId(Long.parseLong(paymentId));
        return sellerClient.getBaseUrl(transaction.getCustomer().getId());
    }

}
