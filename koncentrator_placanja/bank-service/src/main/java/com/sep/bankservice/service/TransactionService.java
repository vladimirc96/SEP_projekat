package com.sep.bankservice.service;

import com.sep.bankservice.dto.PaymentDTO;
import com.sep.bankservice.dto.PaymentStatusDTO;
import com.sep.bankservice.model.Customer;
import com.sep.bankservice.model.PaymentStatus;
import com.sep.bankservice.model.Transaction;
import com.sep.bankservice.repository.TransactionRepository;
import org.bouncycastle.util.test.TestRandomBigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestTemplate restTemplate;

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

    public Transaction create(PaymentDTO paymentDTO, Customer customer){
        Transaction transaction = new Transaction();
        transaction.setAmount(paymentDTO.getAmount());
        transaction.setTimestamp(new Date());
        transaction.setPaymentStatus(PaymentStatus.PROCESSING);

        transaction.setCustomer(customer);
        transaction = transactionRepo.save(transaction);

        final Transaction t = transaction;

        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            Timer timer = new Timer();
            long startTime = System.currentTimeMillis();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){
                    System.out.println("************** LOOP **************");
                    long loopTime = System.currentTimeMillis();;
                    Transaction transactionTemp = transactionRepo.findOneById(t.getId());

                    if(transactionTemp.getPaymentStatus().equals(PaymentStatus.SUCCESS)){
                        System.out.println("************** USPESNA TRANSAKCIJA **************");
                        timer.cancel();
                    }

                    if(transactionTemp.getPaymentStatus().equals(PaymentStatus.FAILURE) || transactionTemp.getPaymentStatus().equals(PaymentStatus.INSUFFCIENT_FUNDS)){
                        System.out.println("************** NEUSPESNA TRANSAKCIJA **************");
                        timer.cancel();
                    }

                    // ako se ne promeni status posle 10 minuta onda zavrsi transakciju
                    if(loopTime - startTime > 600000){
                        System.out.println("************** NEUSPESNA TRANSAKCIJA - PROSLO 10 MIN **************");
                        transactionTemp.setPaymentStatus(PaymentStatus.FAILURE);
                        transactionTemp = transactionRepo.save(transactionTemp);
                        // posalji svima da azuriraju stanje transakcije
                        requestUpdateTransactionBank(transactionTemp);
                        requestUpdateTransactionPcc(transactionTemp);
                        timer.cancel();
                    }
                }
            },5000,10000);
            return "OK";
        });
        return transaction;
    }


    private void requestUpdateTransactionBank(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://localhost:8450/bank/transaction" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }
    private void requestUpdateTransactionPcc(Transaction transaction){
        HttpEntity<PaymentStatusDTO> entity = new HttpEntity<PaymentStatusDTO>(new PaymentStatusDTO(transaction.getPaymentStatus()));
        ResponseEntity<String> responseEntityPcc = restTemplate.exchange("https://localhost:8452/pcc/transaction/" + transaction.getId(),
                HttpMethod.PUT, entity, String.class);
    }

}
