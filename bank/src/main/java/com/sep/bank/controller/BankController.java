package com.sep.bank.controller;

import com.sep.bank.client.TransactionClient;
import com.sep.bank.dto.*;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Customer;
import com.sep.bank.model.Transaction;
import com.sep.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
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

    @Autowired
    private TransactionClient transactionClient;

    @Autowired
    private CustomerService customerService;

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
        Customer customer = customerService.findOneByPan(pccRequestDTO.getBankAccountDTO().getPan());
        Transaction transaction = transactionService.create(pccRequestDTO, Long.parseLong(id), customer);
        BankAccount bankAccount = bankAccountService.findOneByPan(pccRequestDTO.getBankAccountDTO().getPan());
        return bankAccountService.issuerValidateAndReserve(transaction, bankAccount, pccRequestDTO);
    }

    @RequestMapping(value = "/transfer-funds/{acquirerOrderId}", method = RequestMethod.PUT, consumes = "application/json", produces = "applicatin/json")
    public ResponseEntity<?> transferFunds(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("acquirerOrderId") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        Customer customer = transaction.getCustomer();
        bankAccountService.addFunds(customer.getBankAccount(), transaction);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    // izvrsava placanje
    // promeniti DTO objekat koji prima - potreban prenos ACQUIRER_TIMESTAMP podatka
    @RequestMapping(value = "/confirm-payment/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> payment(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("id") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());

        try{
            transaction = transactionService.executePayment(transaction, bankAccount);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        transactionClient.updateTransactionBankService(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));
        transactionClient.updateTransactionPcc(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));

        logger.logInfo("SUCCESS: Uspesna potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }




}
