package org.sep.newtestservice.service;

import org.sep.newtestservice.client.OrderClient;
import org.sep.newtestservice.dto.FinalizeOrderDTO;
import org.sep.newtestservice.dto.PaymentDTO;
import org.sep.newtestservice.enums.Enums;
import org.sep.newtestservice.model.Seller;
import org.sep.newtestservice.model.Transaction;
import org.sep.newtestservice.repository.SellerRepository;
import org.sep.newtestservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository _transactionRepo;

    @Autowired
    SellerRepository _sellerRepo;

    @Autowired
    OrderClient orderClient;


    public PaymentDTO createPayment(PaymentDTO pDTO) {

        Seller s = _sellerRepo.findById(pDTO.getSellerId()).get();

        Transaction t = new Transaction();

        t.setActiveOrderId(pDTO.getActiveOrderId());
        t.setAmount(pDTO.getAmount());
        t.setCurrency(pDTO.getCurrency());
        t.setStatus("created");
        t.setSeller(s);
        t.setCreatedAt(new Date(System.currentTimeMillis()));



        t = _transactionRepo.save(t);

        final Transaction tFinal = t;

        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){

                    tFinal.setStatus("success");
                    _transactionRepo.save(tFinal);

                    // here goes client call
                    FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                    foDTO.setOrderStatus(Enums.OrderStatus.SUCCESS);
                    foDTO.setActiveOrderId(tFinal.getActiveOrderId());
                    orderClient.finalizeOrder(foDTO);

                    timer.cancel();

                }
            },30000,10000);
            return "OK";
        });


        return PaymentDTO.formDto(t);



    }
}
