package com.sep.bankservice.client;

import com.sep.bankservice.dto.PaymentRequestDTO;
import com.sep.bankservice.dto.RedirectDTO;
import com.sep.bankservice.model.Customer;
import com.sep.bankservice.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BankClient {

    private static final String bankUrl = "https://localhost:8450/bank/check-payment-request";
    private static final String returnUrl = "https://localhost:8500/bank-service/bank/transaction";

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<RedirectDTO> forwardPaymentRequest(Transaction transaction, Customer customer, String merchantPasswordDecrypted){
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(customer.getMerchantId(),
                merchantPasswordDecrypted,
                transaction.getAmount(), transaction.getId(), transaction.getTimestamp(), this.returnUrl);
        // poslati zahtev banci
        HttpEntity<PaymentRequestDTO> entity = new HttpEntity<>(paymentRequestDTO);
        ResponseEntity<RedirectDTO> responseEntity = restTemplate.exchange(this.bankUrl, HttpMethod.PUT, entity, RedirectDTO.class);
        return responseEntity;
    }

}
