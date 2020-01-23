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
import org.springframework.http.*;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(value = "/bank")
public class BankController {

    public Logging logger = new Logging(this);

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
    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<RedirectDTO> check(@RequestBody PaymentRequestDTO paymentRequest){
       return transactionService.checkPaymentRequest(paymentRequest);
    }

    // validira podatke za placanje unete na sajtu banke i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/acquirer/payment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> acquirerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        // ako nisu iste banke, prosledi zahtev PCC-u
        if(!bankAccountService.isBankSame(transaction, bankAccount)){
            PccRequestDTO pccRequestDTO = new PccRequestDTO(transaction.getId(), new Date(), transaction.getAmount(),
                    transaction.getPaymentStatus(), bankAccountDTO);
            HttpEntity<PccRequestDTO> httpEntity = new HttpEntity<PccRequestDTO>(pccRequestDTO);
            ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.postForEntity("https://localhost:8452/pcc/forward-payment", httpEntity, IssuerResponseDTO.class);
            return responseEntity;
        }
        return bankAccountService.acquirerValidateAndReserve(transaction, bankAccount, bankAccountDTO);
    }

    // validira podatke u ulozi issuer banke (banke kupca) i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/issuer/payment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<IssuerResponseDTO> issuerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        return bankAccountService.issuerValidateAndReserve(transaction, bankAccount, bankAccountDTO);
    }

    // izvrsava placanje
    // promeniti DTO objekat koji prima - potreban prenos ACQUIRER_TIMESTAMP podatka
    @RequestMapping(value = "/confirm-payment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> payment(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        logger.logInfo("INFO: Potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());

        transaction = transactionService.executePayment(transaction, bankAccount);
        requestUpdateTransactionBankService(transaction);
        requestUpdateTransactionPcc(transaction);

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(transaction.getId(), transaction.getId(),
                transaction.getId(), transaction.getTimestamp(), transaction.getPaymentStatus());

        //proslediti info KP-u

        logger.logInfo("SUCCESS: Uspesna potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>(paymentResponseDTO, HttpStatus.OK);
    }

    @RequestMapping(value  = "/transaction/{id}", method = RequestMethod.PUT)
    private ResponseEntity<String> updateTransaction(@RequestBody PaymentStatusDTO paymentStatusDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(paymentStatusDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);
        System.out.println(transaction);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }


    private void requestUpdateTransactionBankService(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://localhost:8500/bank-service/bank/transaction/" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }
    private void requestUpdateTransactionPcc(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntityPcc = restTemplate.exchange("https://localhost:8452/pcc/transaction/" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }

}
