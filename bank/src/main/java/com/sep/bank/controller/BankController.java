package com.sep.bank.controller;

import com.sep.bank.dto.*;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Transaction;
import com.sep.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping(value = "/bank")
public class BankController {

    public Logging logger = new Logging(this);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;


    // proverava zahtev za placanje
    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<RedirectDTO> check(@RequestBody PaymentRequestDTO paymentRequest){
       return transactionService.checkPaymentRequest(paymentRequest);
    }

    // validira podatke za placanje unete na sajtu banke i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/acquirer/payment/{paymentId}", method = RequestMethod.PUT)
    public ResponseEntity<?> acquirerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id){
        Transaction transaction = transactionService.findOneByPaymentId(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        // ako nisu iste banke, prosledi zahtev PCC-u
        if(!bankAccountService.isBankSame(transaction, bankAccount)){
            PccRequestDTO pccRequestDTO = new PccRequestDTO(transaction.getId(), transaction.getTimestamp(), transaction.getAmount(),
                    transaction.getPaymentStatus(), bankAccountDTO);
            HttpEntity<PccRequestDTO> httpEntity = new HttpEntity<PccRequestDTO>(pccRequestDTO);
            ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.postForEntity("https://localhost:8452/pcc/forward-payment/" + id, httpEntity, IssuerResponseDTO.class);
            return responseEntity;
        }
        return bankAccountService.acquirerValidateAndReserve(transaction, bankAccount, bankAccountDTO);
    }

    // validira podatke u ulozi issuer banke (banke kupca) i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/issuer/payment/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IssuerResponseDTO> issuerValidate(@RequestBody PccRequestDTO pccRequestDTO, @PathVariable("paymentId") String id){
        // napraviti novu transakciju za issuer-a
        BankAccountDTO bankAccountDTO = pccRequestDTO.getBankAccountDTO();
        Transaction transaction = new Transaction();
        transaction.setPaymentId(Long.parseLong(id));
        transaction.setPaymentStatus(pccRequestDTO.getPaymentStatus());
        transaction.setAmount(pccRequestDTO.getAmount());
        transaction.setTimestamp(new Date());

        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        return bankAccountService.issuerValidateAndReserve(transaction, bankAccount, bankAccountDTO);
    }

    // izvrsava placanje
    // promeniti DTO objekat koji prima - potreban prenos ACQUIRER_TIMESTAMP podatka
    @RequestMapping(value = "/confirm-payment/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
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

    @RequestMapping(value  = "/transaction/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    private ResponseEntity<String> updateTransaction(@RequestBody PaymentStatusDTO paymentStatusDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(paymentStatusDTO.getPaymentStatus());
        transaction = transactionService.save(transaction);
        System.out.println(transaction);
        requestUpdateTransactionPcc(transaction);
        return new ResponseEntity<>("Transakcija azurirana.", HttpStatus.OK);
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
