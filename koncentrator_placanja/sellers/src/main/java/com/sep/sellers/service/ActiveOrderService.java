package com.sep.sellers.service;

import com.sep.sellers.dto.InitOrderRequestDTO;
import com.sep.sellers.dto.InitOrderResponseDTO;
import com.sep.sellers.model.ActiveOrder;
import com.sep.sellers.repository.ActiveOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActiveOrderService {

    @Autowired
    private ActiveOrderRepository activeOrderRepo;

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

        return new InitOrderResponseDTO("https://localhost:8500/sellers/sellers/" + activeOrder.getId());
    }


}
