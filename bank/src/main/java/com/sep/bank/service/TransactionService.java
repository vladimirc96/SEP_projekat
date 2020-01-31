package com.sep.bank.service;

import com.sep.bank.client.TransactionClient;
import com.sep.bank.dto.*;
import com.sep.bank.model.*;
import com.sep.bank.model.enums.BankType;
import com.sep.bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private final String SUCCESS_URL = "/bank";
    private final String FAILED_URL = "FAILED";
    private final String ERROR_URL = "";
    public Logging logger = new Logging(this);


    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionClient transactionClient;

    public Transaction create(PccRequestDTO pccRequestDTO, Long paymentId, Customer customer){
        BankAccountDTO bankAccountDTO = pccRequestDTO.getBankAccountDTO();
        Transaction transaction = new Transaction();
        transaction.setPaymentId(paymentId);
        transaction.setPaymentStatus(pccRequestDTO.getPaymentStatus());
        transaction.setAmount(pccRequestDTO.getAmount());
        transaction.setTimestamp(new Date());
        transaction.setCustomer(customer);
        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    public Transaction create(PaymentRequestDTO paymentRequest, Customer customer){
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setTimestamp(new Date());
        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    public ResponseEntity<RedirectDTO> checkPaymentRequest(PaymentRequestDTO paymentRequest, Transaction transaction, Customer customer, Payment payment){
        logger.logInfo("INFO: Provera podataka zahteva. " + paymentRequest.toString());

        if(!isRequestValid(paymentRequest, customer)){
            logger.logError("ERROR: Zahtev nije validan.");
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionRepo.save(transaction);
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
            return new ResponseEntity<>(new RedirectDTO(FAILED_URL, null), HttpStatus.BAD_REQUEST);
        }

        logger.logInfo("SUCCESS: Uspesna provera podataka zahteva. " + paymentRequest.toString());
        return new ResponseEntity<>(new RedirectDTO(SUCCESS_URL, payment.getId()), HttpStatus.OK);
    }

    public Transaction executePayment(Transaction transaction, BankAccount bankAccount){
        logger.logInfo("INFO: Potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccount.toString());
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    public ResponseEntity<PaymentResponseDTO> issuerProcessTransaction(IssuerResponseDTO issuerResponseDTO, Payment payment){
        PaymentResponseDTO paymentResponseDTO =  null;
        Transaction transaction = transactionRepo.findOneById(issuerResponseDTO.getAcquirerOrderId());

        if(issuerResponseDTO.getPaymentStatus().name().equals("SUCCESS")){
            // dodaj sredstva na racun prodavca i vrati odg
            BankAccount bankAccount = transaction.getCustomer().getBankAccount();
            bankAccountService.addFunds(bankAccount, transaction);
            transaction.setPaymentStatus(issuerResponseDTO.getPaymentStatus());
            transaction = transactionRepo.save(transaction);

            paymentResponseDTO = new PaymentResponseDTO(payment.getMerchantOrderId(), issuerResponseDTO.getAcquirerOrderId(), payment.getId(),
                    issuerResponseDTO.getAcquirerTimestamp(), issuerResponseDTO.getPaymentStatus());

        }else if(issuerResponseDTO.getPaymentStatus().name().equals("ERROR")){

            transaction.setPaymentStatus(issuerResponseDTO.getPaymentStatus());
            transaction = transactionRepo.save(transaction);
            paymentResponseDTO = new PaymentResponseDTO(payment.getMerchantOrderId(), issuerResponseDTO.getAcquirerOrderId(), payment.getId(),
                    issuerResponseDTO.getAcquirerTimestamp(), issuerResponseDTO.getPaymentStatus());
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(), transaction.getPaymentStatus()), payment);
            return new ResponseEntity<>(paymentResponseDTO, HttpStatus.BAD_REQUEST);

        }else if(issuerResponseDTO.getPaymentStatus().name().equals("INSUFFICENT_FUNDS") || issuerResponseDTO.getPaymentStatus().name().equals("FAILURE")){

            transaction.setPaymentStatus(issuerResponseDTO.getPaymentStatus());
            transaction = transactionRepo.save(transaction);
            paymentResponseDTO = new PaymentResponseDTO(payment.getMerchantOrderId(), issuerResponseDTO.getAcquirerOrderId(), payment.getId(),
                    issuerResponseDTO.getAcquirerTimestamp(), issuerResponseDTO.getPaymentStatus());
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
            return new ResponseEntity<>(paymentResponseDTO, HttpStatus.CONFLICT);

        }

        transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
        return new ResponseEntity<>(paymentResponseDTO, HttpStatus.OK);
    }

    public ResponseEntity<PaymentResponseDTO> processTransaction(Transaction transaction, Payment payment){
        PaymentResponseDTO paymentResponseDTO =  null;

        if(transaction.getPaymentStatus().name().equals("SUCCESS")){
            // dodaj sredstva na racun prodavca i vrati odg
            BankAccount bankAccount = transaction.getCustomer().getBankAccount();
            bankAccountService.addFunds(bankAccount, transaction);
            executePayment(transaction, bankAccount);
            paymentResponseDTO = new PaymentResponseDTO(payment.getMerchantOrderId(), transaction.getId(), payment.getId(),
                    transaction.getTimestamp(), transaction.getPaymentStatus());

        }else if(transaction.getPaymentStatus().name().equals("ERROR")){

            paymentResponseDTO = new PaymentResponseDTO(payment.getMerchantOrderId(), transaction.getId(), payment.getId(),
                    transaction.getTimestamp(), transaction.getPaymentStatus());
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
            return new ResponseEntity<>(paymentResponseDTO, HttpStatus.BAD_REQUEST);

        }else if(transaction.getPaymentStatus().name().equals("INSUFFICENT_FUNDS") || transaction.getPaymentStatus().name().equals("FAILURE")){

            paymentResponseDTO = new PaymentResponseDTO(payment.getMerchantOrderId(), transaction.getId(), payment.getId(),
                    transaction.getTimestamp(), transaction.getPaymentStatus());
            transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
            return new ResponseEntity<>(paymentResponseDTO, HttpStatus.CONFLICT);

        }
        transactionClient.updateTransactionBankService(new PaymentStatusDTO(payment.getMerchantOrderId(),transaction.getPaymentStatus()), payment);
        return new ResponseEntity<>(paymentResponseDTO, HttpStatus.OK);
    }

    public Transaction findOneById(Long id){
        return transactionRepo.findOneById(id);
    }

    public List<Transaction> findAll(){
        return transactionRepo.findAll();
    }

    public Transaction save(Transaction transaction){
        return transactionRepo.save(transaction);
    }

    public void remove(Long id){
        transactionRepo.deleteById(id);
    }

    private boolean isRequestValid(PaymentRequestDTO paymentRequest, Customer customer){
        if(customer == null){
            return false;
        }
        boolean isMerchanPasswordValid = BCrypt.checkpw(paymentRequest.getMerchantPassword(), customer.getMerchantPassword());
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

}
