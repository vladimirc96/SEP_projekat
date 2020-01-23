package com.sep.bankservice.client;

import com.sep.bankservice.dto.ApproveDTO;
import com.sep.bankservice.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.AccessDeniedException;

@Service
public class RegistrationClient {

    private final String ENDPOINT_URL = "https://localhost:8342/sellers/register/approve";
    private final long paymentMethodId = 1;

    @Autowired
    RestTemplate restTemplate;

    public void approveRegistration(CustomerDTO customerDTO) throws AccessDeniedException {

        ApproveDTO approveDTO = new ApproveDTO(customerDTO.getId(), customerDTO.getPassword(), this.paymentMethodId);

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
