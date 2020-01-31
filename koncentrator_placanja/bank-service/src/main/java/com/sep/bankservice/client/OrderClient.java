package com.sep.bankservice.client;

import com.sep.bankservice.dto.ActiveOrderDTO;
import com.sep.bankservice.dto.FinalizeOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderClient {

    @Autowired
    RestTemplate restTemplate;

    private final String KP_FINALIZE_ORDER_URL = "https://localhost:8342/active-order/finalize";
    private final String KP_SET_ACTIVE_ORDER_STATUS_URL = "https://localhost:8342/active-order/status";

    public void finalizeOrder(FinalizeOrderDTO foDTO)  {

        ResponseEntity response = restTemplate.postForEntity(this.KP_FINALIZE_ORDER_URL, new HttpEntity<>(foDTO),
                FinalizeOrderDTO.class);

    }

    public void setActiveOrderStatus(ActiveOrderDTO foDTO)  {

        restTemplate.put(this.KP_SET_ACTIVE_ORDER_STATUS_URL, new HttpEntity<>(foDTO));

    }


}
