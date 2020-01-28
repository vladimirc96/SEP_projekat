package com.sep.sellers.service;

import com.sep.sellers.dto.ActiveOrderDTO;
import com.sep.sellers.dto.InitOrderRequestDTO;
import com.sep.sellers.dto.InitOrderResponseDTO;
import com.sep.sellers.model.ActiveOrder;
import com.sep.sellers.repository.ActiveOrderRepository;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveOrderService {

    private final static String redirectUrl = "https://localhost:4200/sellers/";

    @Autowired
    private ActiveOrderRepository activeOrderRepo;

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

        return new InitOrderResponseDTO(redirectUrl + activeOrder.getId());
    }


}
