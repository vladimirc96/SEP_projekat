package com.sep.bank.client;

import com.sep.bank.dto.PaymentStatusDTO;
import com.sep.bank.model.Payment;
import com.sep.bank.model.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;

@Service
public class TransactionClient {

    @Autowired
    private RestTemplate restTemplate;

    public void updateTransactionBankService(PaymentStatusDTO paymentStatusDTO, Payment payment){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentStatusDTO> httpEntity = new HttpEntity<>(paymentStatusDTO);
        ResponseEntity responseEntity = restTemplate.exchange(payment.getReturnUrl(), HttpMethod.PUT, httpEntity, String.class);
    }

    public void updateTransactionIssuer(String url, Long transactionId){
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/" + transactionId,HttpMethod.PUT, null, String.class);
    }

    public void updateTransactionPcc(String url, Long transactionId){
        ResponseEntity<String> responseEntity = restTemplate.exchange(url + "/" + transactionId,HttpMethod.PUT, null, String.class);
    }

}
