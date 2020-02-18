package com.sep.sellers.client;

import com.sep.sellers.dto.KPRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NCRegistrationClient {


    @Autowired
    HttpComponentsClientHttpRequestFactory factory;

    public void informRegistrationStatus(KPRegistrationDTO kprDTO) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.postForEntity(kprDTO.getRegistrationStatusCallbackUrl(), new HttpEntity<>(kprDTO),
                KPRegistrationDTO.class);

    }

}
