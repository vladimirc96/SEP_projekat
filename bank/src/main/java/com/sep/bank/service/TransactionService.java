package com.sep.bank.service;

import com.sep.bank.client.TransactionClient;
import com.sep.bank.dto.*;
import com.sep.bank.model.BankAccount;
import com.sep.bank.model.Customer;
import com.sep.bank.model.PaymentStatus;
import com.sep.bank.model.Transaction;
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
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private CustomerService customerService;

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

    public Transaction create(PaymentRequestDTO paymentRequest){
        Transaction transaction = new Transaction();
        Customer customer = customerService.findByMerchantId(paymentRequest.getMerchantId());
        transaction.setCustomer(customer);
        transaction.setPaymentId(paymentRequest.getMerchantOrderId());
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setTimestamp(new Date());
        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction = transactionRepo.save(transaction);
        return transaction;
    }

    public ResponseEntity<RedirectDTO> checkPaymentRequest(PaymentRequestDTO paymentRequest){
        logger.logInfo("INFO: Provera podataka zahteva. " + paymentRequest.toString());
        Transaction transaction = create(paymentRequest);

        if(!isRequestValid(paymentRequest, transaction.getCustomer())){
            logger.logError("ERROR: Zahtev nije validan.");
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionRepo.save(transaction);
            transactionClient.updateTransactionBankService(transaction.getPaymentId(), new PaymentStatusDTO(transaction.getPaymentStatus()));
            return new ResponseEntity<>(new RedirectDTO(FAILED_URL, null), HttpStatus.BAD_REQUEST);
        }

        logger.logInfo("SUCCESS: Uspesna provera podataka zahteva. " + paymentRequest.toString());
        return new ResponseEntity<>(new RedirectDTO(SUCCESS_URL, paymentRequest.getMerchantOrderId()), HttpStatus.OK);
    }

    public Transaction executePayment(Transaction transaction, BankAccount bankAccount){
        logger.logInfo("INFO: Potvrda placanja. Transaction: " + transaction.toString() + "; bank account data: " + bankAccount.toString());
        transaction.setPaymentStatus(PaymentStatus.SUCCESS);
        transaction = transactionRepo.save(transaction);
        return transaction;
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

    public Transaction findOneByPaymentId(Long id){
        return transactionRepo.findOneByPaymentId(id);
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
