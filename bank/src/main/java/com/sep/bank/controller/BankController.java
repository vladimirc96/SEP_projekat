package com.sep.bank.controller;

import com.sep.bank.client.BankClient;
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

    // proverava zahtev za placanje
    @RequestMapping(value = "/check-payment-request", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<RedirectDTO> check(@RequestBody PaymentRequestDTO paymentRequest){
        Customer customer = customerService.findByMerchantId(paymentRequest.getMerchantId());
        Transaction transaction = transactionService.create(paymentRequest, customer);
        Payment payment = paymentService.create(paymentRequest, transaction.getId(), paymentRequest.getMerchantOrderId());
        return transactionService.checkPaymentRequest(paymentRequest, transaction, customer, payment);
    }


    // validira podatke za placanje unete na sajtu banke i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/acquirer/payment/{paymentId}", method = RequestMethod.PUT)
    public ResponseEntity<?> acquirerValidate(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id){
        Payment payment = paymentService.findOneById(Long.parseLong(id));
        Transaction transaction = transactionService.findOneById(payment.getAcquirerOrderId());
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());

        System.out.println("********************************************");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String date = format.format(bankAccountDTO.getExpirationDate());
        System.out.println("EXPIRATION DATE: " + date);
        System.out.println("********************************************");

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
        Payment payment = paymentService.findOneById(Long.parseLong(id));
        // napraviti novu transakciju za issuer-a
        System.out.println("********************************************");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String date = format.format(pccRequestDTO.getBankAccountDTO().getExpirationDate());
        System.out.println("EXPIRATION DATE: " + date);
        System.out.println("********************************************");

        Customer customer = customerService.findOneByPan(pccRequestDTO.getBankAccountDTO().getPan());
        Transaction transaction = transactionService.create(pccRequestDTO, Long.parseLong(id), customer);

        payment.setIssuerOrderId(transaction.getId());
        payment = paymentService.save(payment);

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

    @RequestMapping(value = "/confirm-payment-acquirer/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> paymentAcquirer(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id){
        Payment payment = paymentService.findOneById(Long.parseLong(id));
        Transaction transaction = transactionService.findOneById(payment.getAcquirerOrderId());
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        try{
            transaction = transactionService.executePayment(transaction, bankAccount);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

        transactionClient.updateTransactionBankService(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));

        logger.logInfo("SUCCESS: Uspesna potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @RequestMapping(value = "/confirm-payment-issuer/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> paymentIssuer(@RequestBody BankAccountDTO bankAccountDTO, @PathVariable("paymentId") String id){
        Payment payment = paymentService.findOneById(Long.parseLong(id));
        Transaction transaction = transactionService.findOneById(payment.getIssuerOrderId());
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        try{
            transaction = transactionService.executePayment(transaction, bankAccount);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        transactionClient.updateTransactionPcc(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));

        logger.logInfo("SUCCESS: Uspesna potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccountDTO.toString());
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }




}
