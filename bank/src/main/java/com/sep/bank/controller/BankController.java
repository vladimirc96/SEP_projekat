package com.sep.bank.controller;

import com.sep.bank.client.BankClient;
import com.sep.bank.client.PccClient;
import com.sep.bank.client.TransactionClient;
import com.sep.bank.dto.*;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Customer;
import com.sep.bank.model.Payment;
import com.sep.bank.model.Transaction;
import com.sep.bank.model.enums.BankType;
import com.sep.bank.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.text.ParseException;
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
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionClient transactionClient;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BankClient bankClient;

    @Autowired
    private PccClient pccClient;

    // proverava zahtev za placanje
    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<RedirectDTO> check(@RequestBody PaymentRequestDTO paymentRequest){
        Customer customer = customerService.findByMerchantId(paymentRequest.getMerchantId());
        Transaction transaction = transactionService.create(paymentRequest, customer);
        Payment payment = paymentService.create(paymentRequest, transaction.getId(), paymentRequest.getMerchantOrderId());
        transaction.setPaymentId(payment.getId());
        transaction = transactionService.save(transaction);
        return transactionService.checkPaymentRequest(paymentRequest, transaction, customer, payment);
    }


    // validira podatke za placanje unete na sajtu banke i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/acquirer/payment/{paymentId}", method = RequestMethod.PUT)
    public ResponseEntity<PaymentResponseDTO> acquirerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id) {
        try {
            bankAccountDTO = bankAccountService.parseDate(bankAccountDTO);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Payment payment = paymentService.findOneById(Long.parseLong(id));
        Transaction transaction = transactionService.findOneById(payment.getAcquirerOrderId());
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());

        ResponseEntity<IssuerResponseDTO> responseEntity = null;
        // ako nisu iste banke, prosledi zahtev PCC-u
        if(!bankAccountService.isBankSame(transaction, bankAccount)){
            PccRequestDTO pccRequestDTO = new PccRequestDTO(transaction.getId(), transaction.getTimestamp(), transaction.getAmount(),
                    transaction.getPaymentStatus(), bankAccountDTO);
           responseEntity = pccClient.forward(pccRequestDTO, payment);
        }

        // obrada transakcije - ako ima issuer banka
        if(responseEntity != null){
            payment.setIssuerOrderId(responseEntity.getBody().getIssuerOrderId());
            return transactionService.issuerProcessTransaction(responseEntity.getBody(), payment);
        }

        // obrada transakcije - ako nema issuer banke
        transaction = bankAccountService.acquirerValidateAndReserve(transaction, bankAccount, bankAccountDTO);
        return transactionService.processTransaction(transaction, payment);
    }

    // validira podatke u ulozi issuer banke (banke kupca) i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/issuer/payment/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IssuerResponseDTO> issuerValidate(@RequestBody PccRequestDTO pccRequestDTO, @PathVariable("paymentId") String id){
        // napraviti novu transakciju za issuer-a
        BankAccountDTO bankAccountDTO = pccRequestDTO.getBankAccountDTO();
        try {
            bankAccountDTO = bankAccountService.parseDate(pccRequestDTO.getBankAccountDTO());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Customer customer = customerService.findOneByPan(bankAccountDTO.getPan());
        Transaction transaction = transactionService.create(pccRequestDTO, Long.parseLong(id), customer);
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        return bankAccountService.issuerValidateAndReserve(transaction, bankAccount, pccRequestDTO);
    }

//    @RequestMapping(value = "/transfer-funds/{acquirerOrderId}", method = RequestMethod.PUT, consumes = "application/json", produces = "applicatin/json")
//    public ResponseEntity<?> transferFunds(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("acquirerOrderId") String id){
//        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
//        Customer customer = transaction.getCustomer();
//        bankAccountService.addFunds(customer.getBankAccount(), transaction);
//        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/confirm-payment-acquirer/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
//    public ResponseEntity<?> paymentAcquirer(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id){
//        Payment payment = paymentService.findOneById(Long.parseLong(id));
//        Transaction transaction = transactionService.findOneById(payment.getAcquirerOrderId());
//        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
//        try{
//            transaction = transactionService.executePayment(transaction, bankAccount);
//        }catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//
//        transactionClient.updateTransactionBankService(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));
//
//        logger.logInfo("SUCCESS: Uspesna potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
//        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
//    }
//
//    @RequestMapping(value = "/confirm-payment-issuer/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
//    public ResponseEntity<?> paymentIssuer(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id){
//        Payment payment = paymentService.findOneById(Long.parseLong(id));
//        Transaction transaction = transactionService.findOneById(payment.getIssuerOrderId());
//        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
//        try{
//            transaction = transactionService.executePayment(transaction, bankAccount);
//        }catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
//        }
//        transactionClient.updateTransactionPcc(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));
//
//        logger.logInfo("SUCCESS: Uspesna potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
//        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
//    }




}
