package com.sep.paymentcardcenter.client;

import com.sep.paymentcardcenter.dto.IssuerResponseDTO;
import com.sep.paymentcardcenter.dto.PccRequestDTO;
import com.sep.paymentcardcenter.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankClient {

    private static final String updateUrl = "https://localhost:8452/pcc/transaction-failed";

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<IssuerResponseDTO> forward(PccRequestDTO pccRequestDTO, Transaction transaction, String id){
        HttpEntity<PccRequestDTO> entity = new HttpEntity<>(pccRequestDTO);
        ResponseEntity<IssuerResponseDTO> responseEntity = restTemplate.exchange("https://localhost:8451/bank/issuer/payment/" + id,
                HttpMethod.PUT, entity, IssuerResponseDTO.class);

        responseEntity.getBody().setPccUpdateUrl(this.updateUrl);
        responseEntity.getBody().setPccOrderId(transaction.getId());

        return responseEntity;
    }

}
