package com.sep.bitcoinservice.client;

import com.netflix.ribbon.proxy.annotation.Http;
import com.sep.bitcoinservice.dto.ApproveDTO;
import com.sep.bitcoinservice.dto.CGOrderFullDTO;
import com.sep.bitcoinservice.dto.SellerDTO;
import com.sep.bitcoinservice.model.Seller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.AccessDeniedException;

@Service
public class RegistrationClient {

    private final String ENDPOINT_URL = "https://localhost:8342/sellers/register/approve";
    private final long paymentMethodId = 3;

    @Autowired
    HttpComponentsClientHttpRequestFactory factory;

    public void approveRegistration(SellerDTO sDTO) throws AccessDeniedException {
        RestTemplate restTemplate = new RestTemplate(factory);

        ApproveDTO approveDTO = new ApproveDTO(sDTO.getId(), sDTO.getPassword(), this.paymentMethodId);

        try {
            ResponseEntity response = restTemplate.postForEntity(this.ENDPOINT_URL, new HttpEntity<>(approveDTO),
                    ApproveDTO.class);
            if (response.getStatusCodeValue() != 200) {
                throw new AccessDeniedException("Password is not valid.");
            }
        } catch (HttpClientErrorException e) {
            throw new AccessDeniedException("Password is not valid.");
        }


    }


}
