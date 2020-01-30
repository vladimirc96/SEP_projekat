package com.sep.bank.client;

import com.sep.bank.dto.BankAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class BankClient {

    private static final String bankAcquirer = "https://localhost:8450";
    private static final String bankIssuer = "https://localhost:8451";

    @Autowired
    private RestTemplate restTemplate;

    public void transferFunds(BankAccountDTO bankAccountDTO, Long acquirerOrderId){
        HttpEntity<BankAccountDTO> httpEntity = new HttpEntity<>(bankAccountDTO);
        ResponseEntity responseEntity = restTemplate.exchange(this.bankAcquirer + "/bank/transfer-funds/" + acquirerOrderId, HttpMethod.PUT, httpEntity, String.class);
    }

}
