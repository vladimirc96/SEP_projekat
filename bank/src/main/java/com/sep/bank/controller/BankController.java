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
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<?> check(@RequestBody PaymentRequestDTO paymentRequest){
        Customer customer = customerService.findByMerchantId(paymentRequest.getMerchantId());
        Transaction transaction = new Transaction(paymentRequest.getAmount(), paymentRequest.getMerchantTimestamp(), customer);
        transaction.setId(paymentRequest.getMerchantOrderId());

        if(!isRequestValid(paymentRequest, customer)){
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionService.save(transaction);
            requestUpdateTransactionBankService(transaction);

            return new ResponseEntity<>(new RedirectDTO(FAILED_URL, null), HttpStatus.BAD_REQUEST);
        }

        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction = transactionService.save(transaction);
        return new ResponseEntity<>(new RedirectDTO(SUCCESS_URL, transaction.getId()), HttpStatus.OK);
    }

    // validira podatke za placanje unete na sajtu banke i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/acquirer/validate/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> acquirerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        Customer acquirer = transaction.getCustomer();
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        Customer issuer = bankAccount.getCustomer();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dtoDate = dateFormat.format(bankAccount.getExpirationDate());
        String accDate = dateFormat.format(bankAccountDTO.getExpirationDate());
        System.out.println("DTO DATE " + dtoDate);
        System.out.println("ACC DATE" + accDate);

        // ako nisu iste banke, prosledi zahtev PCC-u
        if(issuer.getBankAccount().getBank().getId() != acquirer.getBankAccount().getBank().getId()){
            PccRequestDTO pccRequestDTO = new PccRequestDTO(transaction.getId(), transaction.getTimestamp(), transaction.getAmount(),
                    transaction.getPaymentStatus(), bankAccountDTO);
            HttpEntity<PccRequestDTO> httpEntity = new HttpEntity<>(pccRequestDTO);
            ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.postForEntity("https://localhost:8452/pcc/forward-payment", httpEntity, IssuerResponseDTO.class);
            return responseEntity;
        }

        try {
            bankAccountService.validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        try {
            bankAccountService.reserveFunds(bankAccount,transaction);
        } catch (Exception e) {
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    // validira podatke u ulozi issuer banke (banke kupca) i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/issuer/validate/{id}", method = RequestMethod.PUT)
    public ResponseEntity<IssuerResponseDTO> issuerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        try {
            bankAccountService.validation(bankAccountDTO, bankAccount, transaction);
        } catch (Exception e) {
            e.printStackTrace();
            requestUpdateTransactionPcc(transaction);
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), transaction.getId(),
                    transaction.getTimestamp(), transaction.getId(), transaction.getTimestamp()), HttpStatus.BAD_REQUEST);
        }
        try {
            bankAccountService.reserveFunds(bankAccount,transaction);
        } catch (Exception e) {
            e.printStackTrace();
            requestUpdateTransactionPcc(transaction);
            requestUpdateTransactionBankService(transaction);
            return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), transaction.getId(),
                    transaction.getTimestamp(), transaction.getId(), transaction.getTimestamp()), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new IssuerResponseDTO(transaction.getPaymentStatus(), transaction.getId(),
                transaction.getTimestamp(), transaction.getId(), transaction.getTimestamp()), HttpStatus.OK);
    }

    // izvrsava placanje
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PaymentResponseDTO> payment(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());

        transaction = transactionService.executePayment(transaction, bankAccount);
        requestUpdateTransactionBankService(transaction);
        requestUpdateTransactionPcc(transaction);

        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO(transaction.getId(), transaction.getId(),
                transaction.getId(), transaction.getTimestamp(), transaction.getPaymentStatus());

        //proslediti info KP-u
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

    private boolean isRequestValid(PaymentRequestDTO paymentRequest, Customer customer){
        if(customer == null){
            return false;
        }
        boolean isMerchanPasswordValid = BCrypt.checkpw(paymentRequest.getMerchantPassword(), customer.getMerchantPassword());
//        Customer customer = customerService.findByMerchantIdAndMerchantPassword(paymentRequest.getMerchantId(),
//                 paymentRequest.getMerchantPassword());
        if(!isMerchanPasswordValid){
            return false;
        }
        if(paymentRequest.getAmount() == 0){
            return false;
        }else if(paymentRequest.getMerchantTimestamp() == null){
            return false;
        }
        return true;
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
