package com.sep.sellers.client;

import com.sep.sellers.dto.FinalizeOrderDTO;
import com.sep.sellers.dto.KPRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Service
public class NCFinalizeClient {

    @Autowired
    HttpComponentsClientHttpRequestFactory factory;

    public void finalizeOrder(FinalizeOrderDTO finalizeOrderDTO, String returnUrl) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.postForEntity(returnUrl, new HttpEntity<>(finalizeOrderDTO),
                FinalizeOrderDTO.class);
    }

}
