package com.sep.bankservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SellerClient {

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<String> getBaseUrl(Long sellerId){
        return restTemplate.getForEntity("https://localhost:8500/sellers/sellers/getWebsiteURL/" + sellerId, String.class);
    }

}
