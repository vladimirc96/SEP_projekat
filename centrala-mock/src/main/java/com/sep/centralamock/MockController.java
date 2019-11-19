package com.sep.centralamock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/mock")
public class MockController {

    @Autowired
    public RestTemplate restTemplate;

    @RequestMapping(value="/test", method = RequestMethod.GET)
    public ResponseEntity<String> test(){
        String body = "Zahtev za placanje";
        HttpEntity<String> HReq=new HttpEntity<String>(body);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8440/test", HReq, String.class);

        return response;
    }

}
