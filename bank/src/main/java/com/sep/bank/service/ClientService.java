package com.sep.bank.service;

import com.sep.bank.model.Client;
import com.sep.bank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepo;

    public Client findOneById(Long id){
        return clientRepo.findOneById(id);
    }

    public List<Client> findAll(){
        return clientRepo.findAll();
    }

    public Client save(Client client){
        return clientRepo.save(client);
    }

    public void remove(Long id){
        clientRepo.deleteById(id);
    }

    public Client findByMerchantId(String id){
        return clientRepo.findByMerchantId(id);
    }

    public Client findByMerchantIdAndMerchantPassword(String id, String password){
        return clientRepo.findByMerchantIdAndMerchantPassword(id, password);
    }



}
