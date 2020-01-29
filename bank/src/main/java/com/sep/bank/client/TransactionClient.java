package com.sep.bank.client;

import com.sep.bank.dto.PaymentStatusDTO;
import com.sep.bank.model.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionClient {

    @Autowired
    private RestTemplate restTemplate;

    public void updateTransactionBankService(Long id, PaymentStatusDTO paymentStatusDTO){
        HttpEntity<PaymentStatusDTO> httpEntity = new HttpEntity<>(paymentStatusDTO);
        ResponseEntity responseEntity = restTemplate.exchange("https://localhost:8500/bank-service/bank/transaction/" + id, HttpMethod.PUT, httpEntity, String.class);
    }

    public void updateTransactionPcc(Long id, PaymentStatusDTO paymentStatusDTO){
        HttpEntity<PaymentStatusDTO> httpEntity = new HttpEntity<>(paymentStatusDTO);
        ResponseEntity responseEntity = restTemplate.exchange("https://localhost:8452/pcc/transaction/" + id, HttpMethod.PUT, httpEntity, String.class);
    }


}
