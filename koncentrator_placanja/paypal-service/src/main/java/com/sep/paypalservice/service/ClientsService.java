package com.sep.paypalservice.service;

import com.sep.paypalservice.client.RegistrationClient;
import com.sep.paypalservice.dto.PPClientDTO;
import com.sep.paypalservice.model.PPClient;
import com.sep.paypalservice.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
public class ClientsService {

    @Autowired
    private ClientsRepository clientsRepo;

    @Autowired
    private RegistrationClient registrationClient;

    @Autowired
    private CryptoService cryptoService;

    @Transactional(rollbackFor = AccessDeniedException.class)
    public PPClientDTO registerSeller(PPClientDTO ppClientDTO) throws AccessDeniedException {
        PPClient ppClient = new PPClient();
        ppClient.setId(ppClientDTO.getId());
        ppClient.setClientId(ppClientDTO.getClientId());
        ppClient.setClientSecret(cryptoService.encrypt(ppClientDTO.getClientSecret()));

        ppClient = clientsRepo.save(ppClient);

        registrationClient.approveRegistration(ppClientDTO);

        return PPClientDTO.formDTO(ppClient);
    }

}
