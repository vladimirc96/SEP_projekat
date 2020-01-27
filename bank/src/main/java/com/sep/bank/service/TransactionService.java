package com.sep.bank.service;

import com.sep.bank.dto.PaymentRequestDTO;
import com.sep.bank.dto.PaymentStatusDTO;
import com.sep.bank.dto.RedirectDTO;
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

    public ResponseEntity<RedirectDTO> checkPaymentRequest(PaymentRequestDTO paymentRequest){
        logger.logInfo("INFO: Provera podataka zahteva. " + paymentRequest.toString());
        Customer customer = customerService.findByMerchantId(paymentRequest.getMerchantId());
        Transaction transaction = new Transaction(paymentRequest.getAmount(), new Date(), customer);
        transaction.setPaymentId(paymentRequest.getMerchantOrderId());

        if(!isRequestValid(paymentRequest, customer)){
            logger.logError("ERROR: Zahtev nije validan.");
            transaction.setPaymentStatus(PaymentStatus.FAILURE);
            transaction = transactionRepo.save(transaction);
            requestUpdateTransactionBankService(paymentRequest.getMerchantOrderId(), PaymentStatus.FAILURE);
            return new ResponseEntity<>(new RedirectDTO(FAILED_URL, null), HttpStatus.BAD_REQUEST);
        }

        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction = transactionRepo.save(transaction);
        logger.logInfo("SUCCESS: Uspesna provera podataka zahteva. " + paymentRequest.toString());
        return new ResponseEntity<>(new RedirectDTO(SUCCESS_URL, paymentRequest.getMerchantOrderId()), HttpStatus.OK);
    }

    public Transaction executePayment(Transaction transaction, BankAccount bankAccount){
        // obraditi transakciju i proslediti podatke MERCHANT_ORDER_ID, ACQUIRER_ORDER_ID, ACQUIRER_TIMESTAMP i PAYMENT_ID
        // ne salju se za sada svi ti podaci posto nema Issuer banke
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


    private void requestUpdateTransactionBankService(Long id, PaymentStatus paymentStatus){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(paymentStatus));
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://localhost:8500/bank-service/bank/transaction/" + id,
                HttpMethod.PUT, entity, String.class);
    }
}
