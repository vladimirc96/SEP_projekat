package com.sep.bankservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="/test")
public class TestController {

    @Autowired
    public RestTemplate restTemplate;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> test(@RequestBody String body){
        HttpEntity<String> HReq=new HttpEntity<String>(body);
        // za Bank
        ResponseEntity<String> response = restTemplate.postForEntity("https://localhost:8450/test", HReq, String.class);
        return response;
    }

}
