package org.sep.newtestservice.service;

import org.sep.newtestservice.client.OrderClient;
import org.sep.newtestservice.dto.ActiveOrderDTO;
import org.sep.newtestservice.dto.FinalizeOrderDTO;
import org.sep.newtestservice.dto.PaymentDTO;
import org.sep.newtestservice.enums.Enums;
import org.sep.newtestservice.model.Seller;
import org.sep.newtestservice.model.Transaction;
import org.sep.newtestservice.repository.SellerRepository;
import org.sep.newtestservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    // Ne bi trebalo da se ovako bas zakuca, trebalo bi napraviti neko polje u bazi koje ce dobiti vrednost
    // ID-a prilikom kreiranja servisa, ali stvarno vise nema vremena
    private static final int paymentMethodId = 4;


    public PaymentDTO createPayment(PaymentDTO pDTO) {

        Seller s = _sellerRepo.findById(pDTO.getSellerId()).get();

        // Inform Seller-service that status is PENDING
        try {
            orderClient.setActiveOrderStatus(new ActiveOrderDTO(pDTO.getActiveOrderId(), Enums.OrderStatus.PENDING,
                    this.paymentMethodId));
        } catch (HttpClientErrorException ex) {
            throw new IllegalStateException("Active order status is already PENDING.");
        }

        Transaction t = new Transaction();

        t.setActiveOrderId(pDTO.getActiveOrderId());
        t.setAmount(pDTO.getAmount());
        t.setCurrency("USD");
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
            },3000,10000);
            return "OK";
        });


        return PaymentDTO.formDto(t);



    }

    public FinalizeOrderDTO getOrderStatus(long id) {
        Transaction t = _transactionRepo.findByActiveOrderId(id).get();

        FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
        foDTO.setActiveOrderId(t.getActiveOrderId());

        System.out.println("[GetOrderStatus]: activeOrderId: " + t.getActiveOrderId() + ", status: " + t.getStatus());


        if (t.getStatus().equals("created")) {
            foDTO.setOrderStatus(Enums.OrderStatus.PENDING);
            System.out.println("[GetOrderStatus]: returning PENDING");
        } else if (t.getStatus().equals("success")) {
            foDTO.setOrderStatus(Enums.OrderStatus.SUCCESS);
            System.out.println("[GetOrderStatus]: returning SUCCESS");
        } else {
            foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
            System.out.println("[GetOrderStatus]: returning FAILED");
        }


        return foDTO;

    }
}
