package com.sep.bank.controller;

import com.sep.bank.client.BankClient;
import com.sep.bank.client.PccClient;
import com.sep.bank.client.TransactionClient;
import com.sep.bank.dto.*;
import com.sep.bank.model.*;
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
        Payment payment = paymentService.findOneById(Long.parseLong(id));
        Transaction transaction = transactionService.findOneById(payment.getAcquirerOrderId());

        if(transaction.getPaymentStatus().name().equals("FAILURE")){
            return new ResponseEntity<>(new PaymentResponseDTO(payment.getMerchantOrderId(), transaction.getId(), payment.getId(),
                    transaction.getTimestamp(), transaction.getPaymentStatus()), HttpStatus.CONFLICT);
        }

        try {
            bankAccountDTO = bankAccountService.parseDate(bankAccountDTO);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // ako nisu iste banke, prosledi zahtev PCC-u
        if(!bankAccountService.isBankSame(transaction, transaction.getCustomer().getBankAccount(), bankAccountDTO)){
            try{
                ResponseEntity<IssuerResponseDTO> responseEntity = pccClient.forward(transaction, bankAccountDTO, payment);
                return transactionService.issuerProcessTransaction(responseEntity.getBody(), payment);
            }catch(Exception e){
                e.printStackTrace();
                transaction.setPaymentStatus(PaymentStatus.FAILURE);
                transaction = transactionService.save(transaction);
                return transactionService.processTransaction(transaction, payment);
            }
        }
        BankAccount bankAccount = bankAccountService.findOneByPan(bankAccountDTO.getPan());
        transaction = bankAccountService.acquirerValidateAndReserve(transaction, bankAccount, bankAccountDTO,payment);
        return transactionService.processTransaction(transaction, payment);
    }

    // validira podatke u ulozi issuer banke (banke kupca) i proverava da li postoji dovoljno sredstava
    @RequestMapping(value = "/issuer/payment/{paymentId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<IssuerResponseDTO> issuerValidate(@RequestBody PccRequestDTO pccRequestDTO, @PathVariable("paymentId") String id){
        BankAccountDTO bankAccountDTO = pccRequestDTO.getBankAccountDTO();
        try {
            bankAccountDTO = bankAccountService.parseDate(pccRequestDTO.getBankAccountDTO());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Customer customer = customerService.findOneByPan(bankAccountDTO.getPan());
        Transaction transaction = transactionService.create(pccRequestDTO, Long.parseLong(id), customer);
        return bankAccountService.issuerValidateAndReserve(transaction, customer.getBankAccount(), pccRequestDTO);
    }


    @RequestMapping(value = "/transaction-failed", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateTransaction(@RequestBody PaymentIdDTO paymentId){
        Payment payment = paymentService.findOneById(paymentId.getId());
        Transaction transaction = transactionService.findOneById(payment.getAcquirerOrderId());
        transaction.setPaymentStatus(PaymentStatus.FAILURE);
        transaction = transactionService.save(transaction);
        transactionService.notifyIsFaield(payment);
        return new ResponseEntity("Updated", HttpStatus.OK);
    }

    @RequestMapping(value = "/transaction-failed/{issuerOrderId}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updateIssuerTransaction(@PathVariable("issuerOrderId") String id){
        Transaction transaction = transactionService.findOneById(Long.parseLong(id));
        transaction.setPaymentStatus(PaymentStatus.FAILURE);
        transaction = transactionService.save(transaction);
        return new ResponseEntity("Updated", HttpStatus.OK);
    }

}
