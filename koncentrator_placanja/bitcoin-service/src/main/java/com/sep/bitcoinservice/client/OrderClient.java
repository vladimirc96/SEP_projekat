package com.sep.bitcoinservice.client;

import com.sep.bitcoinservice.dto.ApproveDTO;
import com.sep.bitcoinservice.dto.FinalizeOrderDTO;
import com.sep.bitcoinservice.dto.SellerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.AccessDeniedException;

@Service
public class OrderClient {

    @Autowired
    RestTemplate restTemplate;

    private final String KP_FINALIZE_ORDER_URL = "https://localhost:8342/sellers/active-order/finalize";

    public void finalizeOrder(FinalizeOrderDTO foDTO)  {

        ResponseEntity response = restTemplate.postForEntity(this.KP_FINALIZE_ORDER_URL, new HttpEntity<>(foDTO),
                FinalizeOrderDTO.class);

    }

}
