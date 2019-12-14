package com.sep.centralamock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@CrossOrigin
@RestController
@RequestMapping(value = "/mock")
public class MockController {

    @Autowired
    public RestTemplate restTemplate;

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public ResponseEntity<String> test(){
        String body = "Zahtev za placanje";
        HttpEntity<String> HReq=new HttpEntity<String>(body);

        // za Bank-service
        ResponseEntity<String> response = restTemplate.postForEntity("https://localhost:8440/bank-service/test", HReq, String.class);

        return response;
    }

}
