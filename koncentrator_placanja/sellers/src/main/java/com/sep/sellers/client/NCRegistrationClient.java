package com.sep.sellers.client;

import com.sep.sellers.dto.KPRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NCRegistrationClient {


    @Autowired
    RestTemplate restTemplate;

    private void informRegistrationStatus(KPRegistrationDTO kprDTO) {
        restTemplate.postForEntity(kprDTO.getRegistrationStatusCallbackUrl(), new HttpEntity<>(kprDTO),
                KPRegistrationDTO.class);

    }

}
