package com.sep.bankservice.service;

import com.sep.bankservice.client.BankClient;
import com.sep.bankservice.client.OrderClient;
import com.sep.bankservice.dto.*;
import com.sep.bankservice.model.Customer;
import com.sep.bankservice.model.Enums;
import com.sep.bankservice.model.PaymentStatus;
import com.sep.bankservice.model.Transaction;
import com.sep.bankservice.repository.TransactionRepository;
import org.bouncycastle.util.test.TestRandomBigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
    private OrderClient orderClient;

    @Autowired
    private BankClient bankClient;

    private final long paymentMethodId = 1;

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

    // ubaci logger
    public Transaction create(ActiveOrderDTO activeOrderDTO, Customer customer){

        // Inform Seller-service that status is PENDING
        try {
            ActiveOrderDTO aoDTOsellers = new ActiveOrderDTO();
            aoDTOsellers.setPaymentMethodId(this.paymentMethodId);
            aoDTOsellers.setId(activeOrderDTO.getId());
            aoDTOsellers.setOrderStatus(Enums.OrderStatus.PENDING);
            orderClient.setActiveOrderStatus(aoDTOsellers);
        } catch (HttpClientErrorException ex) {
            throw new IllegalStateException("Active order status is already PENDING.");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(activeOrderDTO.getAmount());
        transaction.setTimestamp(new Date());
        transaction.setPaymentStatus(PaymentStatus.PROCESSING);
        transaction.setCustomer(customer);
        transaction.setActiveOrderId(activeOrderDTO.getId());
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
                    try{

                        Transaction transactionTemp = transactionRepo.findOneById(t.getId());

                        if(transactionTemp.getPaymentStatus().equals(PaymentStatus.SUCCESS)){
                            System.out.println("************** USPESNA TRANSAKCIJA **************");
                            FinalizeOrderDTO finalizeOrderDTO = new FinalizeOrderDTO();
                            finalizeOrderDTO.setActiveOrderId(transactionTemp.getActiveOrderId());
                            finalizeOrderDTO.setOrderStatus(convertStatus(transactionTemp.getPaymentStatus()));
                            orderClient.finalizeOrder(finalizeOrderDTO);
                            timer.cancel();
                        }

                        if(transactionTemp.getPaymentStatus().equals(PaymentStatus.FAILURE) || transactionTemp.getPaymentStatus().equals(PaymentStatus.INSUFFICIENT_FUNDS)){
                            System.out.println("************** NEUSPESNA TRANSAKCIJA **************");
                            FinalizeOrderDTO finalizeOrderDTO = new FinalizeOrderDTO();
                            finalizeOrderDTO.setActiveOrderId(transactionTemp.getActiveOrderId());
                            finalizeOrderDTO.setOrderStatus(convertStatus(transactionTemp.getPaymentStatus()));
                            orderClient.finalizeOrder(finalizeOrderDTO);
                            timer.cancel();
                        }

                        // ako se ne promeni status posle 10 minuta onda zavrsi transakciju
                        if(loopTime - startTime > 60000){
                            System.out.println("************** NEUSPESNA TRANSAKCIJA - PROSLO 10 MIN **************");
                            transactionTemp.setPaymentStatus(PaymentStatus.FAILURE);
                            transactionTemp = transactionRepo.save(transactionTemp);
                            bankClient.updateTransaction(new PaymentIdDTO(transactionTemp.getPaymentId()));
                            FinalizeOrderDTO finalizeOrderDTO = new FinalizeOrderDTO();
                            finalizeOrderDTO.setActiveOrderId(transactionTemp.getActiveOrderId());
                            finalizeOrderDTO.setOrderStatus(convertStatus(transactionTemp.getPaymentStatus()));
                            orderClient.finalizeOrder(finalizeOrderDTO);
                            timer.cancel();
                        }


                    }catch (Exception e){

                    }


                }
            },5000,10000);
            return "OK";
        });

        return transaction;
    }

    public Enums.OrderStatus convertStatus(PaymentStatus paymentStatus){
        if(paymentStatus.name().equals("PROCESSING")){
            return Enums.OrderStatus.PENDING;
        }else if(paymentStatus.name().equals("FAILURE")){
            return Enums.OrderStatus.FAILED;
        }else if(paymentStatus.name().equals("INSUFFICIENT_FUNDS")){
            return Enums.OrderStatus.FAILED;
        }
        return Enums.OrderStatus.SUCCESS;
    }

    public Transaction setPaymentId(Transaction transaction,Long paymentId) {

        if(paymentId != null){
            transaction.setPaymentId(paymentId);
            transaction = transactionRepo.save(transaction);
        }
        return transaction;
    }
}
