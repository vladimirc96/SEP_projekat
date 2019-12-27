package com.sep.bank.controller;

import com.sep.bank.crypto.Crypto;
import com.sep.bank.crypto.KeyStoreUtil;
import com.sep.bank.dto.*;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Customer;
import com.sep.bank.model.PaymentStatus;
import com.sep.bank.model.Transaction;
import com.sep.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@CrossOrigin
@RestController
@RequestMapping(value = "/bank")
public class BankController {

    private final String SUCCESS_URL = "/bank";
    private final String FAILED_URL = "FAILED";
    private final String ERROR_URL = "";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CryptoService cryptoService;

    // proverava zahtev za placanje
    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT)
    public ResponseEntity<?> check(@RequestBody PaymentRequestDTO paymentRequest){
        Customer customer = customerService.findByMerchantId(paymentRequest.getMerchantId());
        Transaction transaction = new Transaction(paymentRequest.getAmount(), paymentRequest.getMerchantTimestamp(), customer);
        transaction.setId(paymentRequest.getMerchantOrderId());

        if(!isRequestValid(paymentRequest)){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);

            // azuriraj stanje transakcije u KP-u
            requestUpdateTransaction(transaction);

            return new ResponseEntity<>(new RedirectDTO(FAILED_URL, null), HttpStatus.BAD_REQUEST);
        }

        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction = transactionService.save(transaction);
        return new ResponseEntity<>(new RedirectDTO(SUCCESS_URL, transaction.getId()), HttpStatus.OK);
    }

    // validira podatke za placanje unete na sajtu banke
    // naknadno je potrebno dodati proveru da li je bank prodavca i kupca ista
    @RequestMapping(value = "/validate/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> validate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));

        // provera ostalih podataka
        BankAccount bankAccount = bankAccountService.validate(bankAccountDTO);
        if(bankAccount == null){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);

            // azuriraj stanje transakcije u KP-u
            requestUpdateTransaction(transaction);

            return new ResponseEntity<>("FAIL: THE DATA ENTERED IS NOT VALID", HttpStatus.BAD_REQUEST);
        }

        // proveriti da li je istekla kartica
        if(bankAccountService.isExpired(bankAccountDTO.getExpirationDate())){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);

            // azuriraj stanje transakcije u KP-u
            requestUpdateTransaction(transaction);

            return new ResponseEntity<>("CARD IS EXPIRED", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }


    // izvrsava placanje
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PaymentResponseDTO> payment(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());

        //provera raspolozivih sredstava
        if(!bankAccountService.hasFunds(bankAccount.getBalance(), transaction.getAmount())){
            transaction.setPaymentStatus(PaymentStatus.INSUFFCIENT_FUNDS);
            transaction = transactionService.save(transaction);
            requestUpdateTransaction(transaction);
            PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(transaction.getId(), transaction.getId(),
                    transaction.getId(), null, transaction.getPaymentStatus());

            return new ResponseEntity<>(paymentResponseDTO, HttpStatus.BAD_REQUEST);
        }else{
            // rezervisi sredstva
            bankAccount.setReserved(transaction.getAmount());
        }

        // skinuti sredstva sa racuna
        bankAccount.setBalance(bankAccount.getBalance()-bankAccount.getReserved());
        bankAccount = bankAccountService.save(bankAccount);

        // obraditi transakciju i proslediti podatke MERCHANT_ORDER_ID, ACQUIRER_ORDER_ID, ACQUIRER_TIMESTAMP i PAYMENT_ID
        // ne salju se za sada svi ti podaci posto nema Issuer banke
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionService.save(transaction);
        requestUpdateTransaction(transaction);

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(transaction.getId(), transaction.getId(),
                transaction.getId(), null, transaction.getPaymentStatus());


        return new ResponseEntity<>(paymentResponseDTO, HttpStatus.OK);
    }

    private boolean isRequestValid(PaymentRequestDTO paymentRequest){
        String merchantPassEncrypted = cryptoService.encrypt(paymentRequest.getMerchantPassword());
        Customer customer = customerService.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(), merchantPassEncrypted);
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

    private void requestUpdateTransaction(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://localhost:8500/bank-service/bank/transaction/" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }

}
