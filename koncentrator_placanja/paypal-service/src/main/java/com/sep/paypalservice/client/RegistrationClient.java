package com.sep.paypalservice.client;

import com.sep.paypalservice.dto.ApproveDTO;
import com.sep.paypalservice.dto.PPClientDTO;
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
    private final long paymentMethodId = 2;

    @Autowired
    HttpComponentsClientHttpRequestFactory requestFactory;

    public void approveRegistration(PPClientDTO ppClientDTO) throws AccessDeniedException {
        RestTemplate restTemplate = new RestTemplate(this.requestFactory);
        ApproveDTO approveDTO = new ApproveDTO(ppClientDTO.getId(), ppClientDTO.getPassword(), this.paymentMethodId);

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
