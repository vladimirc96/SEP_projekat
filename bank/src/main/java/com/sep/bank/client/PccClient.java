package com.sep.bank.client;

import com.sep.bank.dto.BankAccountDTO;
import com.sep.bank.dto.IssuerResponseDTO;
import com.sep.bank.dto.PccRequestDTO;
import com.sep.bank.model.Payment;
import com.sep.bank.model.Transaction;
import com.sep.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PccClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentService paymentService;

    public ResponseEntity<IssuerResponseDTO> forward(Transaction transaction, BankAccountDTO bankAccountDTO, Payment payment){
        PccRequestDTO pccRequestDTO = new PccRequestDTO(transaction.getId(), transaction.getTimestamp(), transaction.getAmount(),
                transaction.getPaymentStatus(), bankAccountDTO);
        HttpEntity<PccRequestDTO> httpEntity = new HttpEntity<PccRequestDTO>(pccRequestDTO);
        ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.postForEntity("https://localhost:8452/pcc/forward-payment/" + payment.getId(), httpEntity, IssuerResponseDTO.class);
        payment.setIssuerOrderId(responseEntity.getBody().getIssuerOrderId());
        payment.setIssuerUpdateUrl(responseEntity.getBody().getIssuerUpdateUrl());
        payment.setPccOrderId(responseEntity.getBody().getPccOrderId());
        payment.setPccUrlUpdate(responseEntity.getBody().getPccUpdateUrl());
        payment = paymentService.save(payment);
        return responseEntity;
    }


}
