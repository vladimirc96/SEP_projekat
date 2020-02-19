package com.sep.sellers.client;

import com.sep.sellers.dto.FinalizeOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NCOrderStatusClient {

    @Autowired
    RestTemplate restTemplate;

    public FinalizeOrderDTO getActiveOrderStatus(String endpointUrl) {
        ResponseEntity<FinalizeOrderDTO> response = restTemplate.exchange(endpointUrl,
                HttpMethod.GET, HttpEntity.EMPTY,
                FinalizeOrderDTO.class);

        return (FinalizeOrderDTO) response.getBody();
    }
}
