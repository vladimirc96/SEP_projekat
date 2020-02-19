package com.sep.sellers.service;

import com.sep.sellers.client.NCFinalizeClient;
import com.sep.sellers.client.NCOrderStatusClient;
import com.sep.sellers.dto.ActiveOrderDTO;
import com.sep.sellers.dto.FinalizeOrderDTO;
import com.sep.sellers.dto.InitOrderRequestDTO;
import com.sep.sellers.dto.InitOrderResponseDTO;
import com.sep.sellers.enums.Enums;
import com.sep.sellers.model.ActiveOrder;
import com.sep.sellers.model.PaymentMethod;
import com.sep.sellers.model.Seller;
import com.sep.sellers.repository.ActiveOrderRepository;
import com.sep.sellers.repository.PaymentMethodRepository;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class ActiveOrderService {

    private final static String redirectUrl = "https://192.168.43.124:4200/sellers/";
    private final static String redirectSubUrl = "https://192.168.43.124:4200/subscriptions/";

    private final static String orderStatusEndpointUrl = "order-status";

    @Autowired
    private ActiveOrderRepository activeOrderRepo;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private NCFinalizeClient ncFinalizeClient;

    @Autowired
    private NCOrderStatusClient ncOrderStatusClient;

    public ActiveOrderDTO findOneById(Long id){
        ActiveOrder activeOrder = activeOrderRepo.findOneById(id);
        return new ActiveOrderDTO(activeOrder.getId(),activeOrder.getNc_order_id(),activeOrder.getTitle(), activeOrder.getCurrency(), activeOrder.getSeller_id(),
                activeOrder.getAmount(), activeOrder.getReturn_url(), activeOrder.getOrderType(),
                activeOrder.getOrderStatus(), activeOrder.getPaymentMethodId(), activeOrder.getUsername());
    }

    public InitOrderResponseDTO create(InitOrderRequestDTO initOrderRequestDTO){
        ActiveOrder activeOrder = new ActiveOrder();
        activeOrder.setAmount(initOrderRequestDTO.getAmount());
        activeOrder.setCurrency(initOrderRequestDTO.getCurrency());
        activeOrder.setNc_order_id(initOrderRequestDTO.getNcOrderId());
        activeOrder.setOrderStatus(Enums.OrderStatus.CREATED);
        activeOrder.setOrderType(initOrderRequestDTO.getOrderType());
        activeOrder.setTitle(initOrderRequestDTO.getTitle());
        activeOrder.setSeller_id(initOrderRequestDTO.getSellerId());
        activeOrder.setReturn_url(initOrderRequestDTO.getReturnUrl());
        activeOrder.setUsername(initOrderRequestDTO.getUsername());

        activeOrder = activeOrderRepo.save(activeOrder);

        setTimerForCheckingOrderStatus(activeOrder.getId());

        if(initOrderRequestDTO.getOrderType().toString() == "ORDER_SUBSCRIPTION") {
            System.out.println("USAO U REDIRECT SUB URL: " + redirectSubUrl + activeOrder.getId());
            return new InitOrderResponseDTO(redirectSubUrl + activeOrder.getId());
        }

        return new InitOrderResponseDTO(redirectUrl + activeOrder.getId());
    }

    // Tajmer koji nakon 10 minuta proverava status ActiveOrder instance i ukoliko je i dalje
    // CREATED stavlja na FAILED jer nije nastavljen proces placanja
    // ako je PENDING trebalo bi poslati zahtev mikroservisu na osnovu polja paymentMethodId i proveriti
    // sta se desava pa na osnovu toga dalje delovati
    private void setTimerForCheckingOrderStatus(long activeOrderId) {
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){

                    ActiveOrder ao = activeOrderRepo.findOneById(activeOrderId);
                    if (ao.getOrderStatus() == Enums.OrderStatus.CREATED) {
                        FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                        foDTO.setNcOrderId(ao.getNc_order_id());
                        foDTO.setActiveOrderId(activeOrderId);
                        foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                        finalizeOrder(foDTO);
                        System.out.println("Setting order status to FAILED for ActiveOrder id: " + ao.getId() + ". " +
                                "Time is up. Sorry " +
                                "but no " +
                                "sorry.");

                        timer.cancel();
                    } else if (ao.getOrderStatus() == Enums.OrderStatus.PENDING) {

                        PaymentMethod pm = paymentMethodRepository.findById(ao.getPaymentMethodId()).get();
                        String endpointUrl = pm.getServiceBaseUrl();
                        if (!endpointUrl.endsWith("/")) {
                            endpointUrl = endpointUrl.concat("/");
                        }

                        endpointUrl = endpointUrl.concat(orderStatusEndpointUrl + "/" + ao.getId());


                        try {
                            FinalizeOrderDTO foDTO = ncOrderStatusClient.getActiveOrderStatus(endpointUrl);
                            System.out.println("[CheckingOrderStatus]: returned status: " + foDTO.getOrderStatus());

                            if (foDTO.getOrderStatus() == Enums.OrderStatus.SUCCESS
                                    || foDTO.getOrderStatus() == Enums.OrderStatus.FAILED) {
                                finalizeOrder(foDTO);
                                System.out.println("[CheckingOrderStatus]: returned final status: " + foDTO.getOrderStatus() + ", finalizing order.");
                                timer.cancel();
                            }
                        } catch (Exception e) {
                            FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                            foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                            foDTO.setActiveOrderId(ao.getId());
                            finalizeOrder(foDTO);
                            System.out.println("[CheckingOrderStatus]: exception, setting status to FAILED");
                        }




                    } else {
                        timer.cancel();
                    }


                }
            },30000,10000);
            return "OK";
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void finalizeOrder(FinalizeOrderDTO foDTO) {
        ActiveOrder ao = activeOrderRepo.findOneById(foDTO.getActiveOrderId());
        foDTO.setNcOrderId(ao.getNc_order_id());
        ao.setOrderStatus(foDTO.getOrderStatus());

        if (ao.getOrderStatus() == Enums.OrderStatus.SUCCESS) {
            if (ao.getOrderType() == Enums.OrderType.ORDER_SUBSCRIPTION) {
                if (foDTO.getFinalDate() == null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(System.currentTimeMillis()));
                    c.add(Calendar.DATE, 30);
                    foDTO.setFinalDate(c.getTime());
                    System.out.println("FINAL date regular subscription: " + foDTO.getFinalDate());
                }

            }
        }

        activeOrderRepo.save(ao);
        ncFinalizeClient.finalizeOrder(foDTO, ao.getReturn_url());
    }

    // Used for setting status to PENDING and returnin error HTTP status if already PENDING
    public void setActiveOrderStatus(ActiveOrderDTO aoDTO) throws IllegalStateException{
        ActiveOrder ao = activeOrderRepo.findOneById(aoDTO.getId());

        if (ao.getOrderStatus() != Enums.OrderStatus.CREATED) {
            throw new IllegalStateException("Active order status is already PENDING.");
        }

        if (aoDTO.getOrderStatus() == Enums.OrderStatus.PENDING) {
            ao.setOrderStatus(aoDTO.getOrderStatus());
            ao.setPaymentMethodId(aoDTO.getPaymentMethodId());
        }
        activeOrderRepo.save(ao);

    }
}
