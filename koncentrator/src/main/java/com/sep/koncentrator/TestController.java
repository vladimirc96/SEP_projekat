package com.sep.koncentrator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
        String requestBody = "Zahtev za placanje";
        HttpEntity<String> HReq=new HttpEntity<String>(requestBody);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8442/test", HReq, String.class);

        return response;
    }
}
