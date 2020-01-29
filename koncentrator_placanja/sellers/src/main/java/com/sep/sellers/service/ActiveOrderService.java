package com.sep.sellers.service;

import com.sep.sellers.client.NCFinalizeClient;
import com.sep.sellers.dto.ActiveOrderDTO;
import com.sep.sellers.dto.FinalizeOrderDTO;
import com.sep.sellers.dto.InitOrderRequestDTO;
import com.sep.sellers.dto.InitOrderResponseDTO;
import com.sep.sellers.enums.Enums;
import com.sep.sellers.model.ActiveOrder;
import com.sep.sellers.repository.ActiveOrderRepository;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

@Service
public class ActiveOrderService {

    private final static String redirectUrl = "https://localhost:4200/sellers/";
    private final static String redirectPlanUrl = "https://localhost:4200/sellersasdasdasd/";


    @Autowired
    private ActiveOrderRepository activeOrderRepo;

    @Autowired
    private NCFinalizeClient ncFinalizeClient;

    public ActiveOrderDTO findOneById(Long id){
        ActiveOrder activeOrder = activeOrderRepo.findOneById(id);
        return new ActiveOrderDTO(activeOrder.getId(),activeOrder.getNc_order_id(),activeOrder.getTitle(), activeOrder.getCurrency(), activeOrder.getSeller_id(),
                activeOrder.getAmount(), activeOrder.getReturn_url(), activeOrder.getOrderType(), activeOrder.getOrderStatus());
    }

    public InitOrderResponseDTO create(InitOrderRequestDTO initOrderRequestDTO){
        ActiveOrder activeOrder = new ActiveOrder();
        activeOrder.setAmount(initOrderRequestDTO.getAmount());
        activeOrder.setCurrency(initOrderRequestDTO.getCurrency());
        activeOrder.setNc_order_id(initOrderRequestDTO.getNcOrderId());
        activeOrder.setOrderStatus(initOrderRequestDTO.getOrderStatus());
        activeOrder.setOrderType(initOrderRequestDTO.getOrderType());
        activeOrder.setTitle(initOrderRequestDTO.getTitle());
        activeOrder.setSeller_id(initOrderRequestDTO.getSellerId());
        activeOrder.setReturn_url(initOrderRequestDTO.getReturnUrl());

        activeOrder = activeOrderRepo.save(activeOrder);

        setTimerForCheckingOrderStatus(activeOrder.getId());

        return new InitOrderResponseDTO(redirectUrl + activeOrder.getId());
    }

    // Tajmer koji nakon 20 minuta proverava status ActiveOrder instance i ukoliko je i dalje
    // PENDING, salje zahtev
    private void setTimerForCheckingOrderStatus(long activeOrderId) {
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(() -> {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask(){
                @Override
                public void run(){

                    ActiveOrder ao = activeOrderRepo.findOneById(activeOrderId);
                    if (ao.getOrderStatus() == Enums.OrderStatus.PENDING) {
                        FinalizeOrderDTO foDTO = new FinalizeOrderDTO();
                        foDTO.setNcOrderId(ao.getNc_order_id());
                        foDTO.setActiveOrderId(activeOrderId);
                        foDTO.setOrderStatus(Enums.OrderStatus.FAILED);
                        finalizeOrder(foDTO);
                    }

                    timer.cancel();
                }
            },900000,0);
            return "OK";
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void finalizeOrder(FinalizeOrderDTO foDTO) {
        ActiveOrder ao = activeOrderRepo.findOneById(foDTO.getActiveOrderId());
        foDTO.setNcOrderId(ao.getNc_order_id());
        ao.setOrderStatus(foDTO.getOrderStatus());
        activeOrderRepo.save(ao);
        ncFinalizeClient.finalizeOrder(foDTO, ao.getReturn_url());
    }
}
